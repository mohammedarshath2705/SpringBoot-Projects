package com.ewallet.heropay.transactionservice.service;

import com.ewallet.heropay.transactionservice.dto.TransactionCreateRequest;
import com.ewallet.heropay.transactionservice.model.Transaction;
import com.ewallet.heropay.transactionservice.model.TransactionStatus;
import com.ewallet.heropay.transactionservice.repository.TransactionRepository; // Fix typo here
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository; // Fixed typo
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String TRANSACTION_CREATED_TOPIC = "transaction_created";
    private static final String TRANSACTION_COMPLETED_TOPIC = "transaction_completed";
    private static final String WALLET_UPDATED_TOPIC = "wallet_updated";
    private static final String WALLET_UPDATE_SUCCESS_STATUS = "SUCCESSFUL";
    private static final String WALLET_UPDATE_FAILED_STATUS = "FAILED";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    // Method to handle transaction creation
    public String transact(TransactionCreateRequest request) throws JsonProcessingException {
        Transaction transaction = Transaction.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .externalId(UUID.randomUUID().toString())
                .transactionStatus(TransactionStatus.PENDING)
                .reason(request.getReason())
                .amount(request.getAmount())
                .build();

        transactionRepository.save(transaction);

        // Preparing message for Kafka
        JSONObject obj = new JSONObject();
        obj.put("senderId", transaction.getSenderId());
        obj.put("receiverId", transaction.getReceiverId());
        obj.put("amount", transaction.getAmount());
        obj.put("transactionId", transaction.getExternalId());

        // Sending message to Kafka
        kafkaTemplate.send(TRANSACTION_CREATED_TOPIC, objectMapper.writeValueAsString(obj));

        return transaction.getExternalId();
    }

    // Kafka listener method to process wallet updates
    @KafkaListener(topics = {WALLET_UPDATED_TOPIC}, groupId = "jbdl123")
    public void updateTransaction(String msg) throws JsonProcessingException {
        JSONObject obj = objectMapper.readValue(msg, JSONObject.class);
        String externalTransactionId = (String) obj.get("transactionId");
        String receiverPhoneNumber = (String) obj.get("receiverWalletId");
        String senderPhoneNumber = (String) obj.get("senderWalletId");
        String walletUpdateStatus = (String) obj.get("status");
        Long amount = (Long) obj.get("amount");

        TransactionStatus transactionStatus;

        // Update transaction status based on wallet update result
        if (walletUpdateStatus.equals(WALLET_UPDATE_FAILED_STATUS)) {
            transactionStatus = TransactionStatus.FAILED;
            transactionRepository.updateTransaction(externalTransactionId, transactionStatus);
        } else {
            transactionStatus = TransactionStatus.SUCCESSFUL;
            transactionRepository.updateTransaction(externalTransactionId, transactionStatus);
        }

        // Fetching user information (email) from external user service
        JSONObject senderObj = this.restTemplate.getForObject("http://localhost:9000/user/phone/" + senderPhoneNumber, JSONObject.class);
        JSONObject receiverObj = this.restTemplate.getForObject("http://localhost:9000/user/phone/" + receiverPhoneNumber, JSONObject.class);

        String senderEmail = senderObj != null ? (String) senderObj.get("email") : null;
        String receiverEmail = receiverObj != null ? (String) receiverObj.get("email") : null;

        // Prepare message for Kafka and send completion update
        obj.put("transactionStatus", transactionStatus.toString());
        obj.put("senderEmail", senderEmail);
        obj.put("receiverEmail", receiverEmail);
        obj.put("senderPhone", senderPhoneNumber);
        obj.put("receiverPhone", receiverPhoneNumber);

        kafkaTemplate.send(TRANSACTION_COMPLETED_TOPIC, objectMapper.writeValueAsString(obj));
    }
}
