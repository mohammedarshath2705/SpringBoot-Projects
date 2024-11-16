package com.ewallet.heropay.walletservice.service;

import com.ewallet.CommonConstants;
import com.ewallet.heropay.walletservice.model.Wallet;
import com.ewallet.heropay.walletservice.repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${wallet.initial.balance}")
    private Long balance;

    @Value("${topics.userCreated}")
    private String userCreatedTopic;

    @Value("${topics.transactionCreated}")
    private String transactionCreatedTopic;

    @Value("${topics.walletUpdated}")
    private String walletUpdatedTopic;

    @KafkaListener(topics = "#{T(java.util.Collections).singletonList('${topics.userCreated}')}", groupId = "jbdl123")
    public void createWallet(String msg) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(msg);

        String walletId = (String) object.get("phone");
        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .currency("INR")
                .balance(balance)
                .build();

        walletRepository.save(wallet);
    }

    @KafkaListener(topics = "#{T(java.util.Collections).singletonList('${topics.transactionCreated}')}", groupId = "jbdl123")
    public void updateWallet(String msg) throws ParseException, JsonProcessingException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(msg);

        String receiverWalletId = (String) obj.get("receiverId");
        String senderWalletId = (String) obj.get("senderId");
        Long amount = (Long) obj.get("amount");
        String transactionId = (String) obj.get("transactionId");

        try {
            Wallet senderWallet = walletRepository.findByWalletId(senderWalletId);
            Wallet receiverWallet = walletRepository.findByWalletId(receiverWalletId);

            if (senderWallet == null || receiverWallet == null || senderWallet.getBalance() < amount) {
                obj = prepareTransactionMessage(receiverWalletId, senderWalletId, amount, transactionId, "FAILED");
                obj.put("senderWalletBalance", senderWallet == null ? 0 : senderWallet.getBalance());
                kafkaTemplate.send(walletUpdatedTopic, objectMapper.writeValueAsString(obj));
                return;
            }

            walletRepository.updateWallet(senderWalletId, -amount);
            walletRepository.updateWallet(receiverWalletId, amount);

            obj = prepareTransactionMessage(receiverWalletId, senderWalletId, amount, transactionId, "SUCCESS");
            kafkaTemplate.send(walletUpdatedTopic, objectMapper.writeValueAsString(obj));

        } catch (Exception e) {
            obj = prepareTransactionMessage(receiverWalletId, senderWalletId, amount, transactionId, "FAILED");
            obj.put("errorMsg", e.getMessage());
            kafkaTemplate.send(walletUpdatedTopic, objectMapper.writeValueAsString(obj));
        }
    }

    private JSONObject prepareTransactionMessage(String receiverId, String senderId, Long amount, String transactionId, String status) {
        JSONObject obj = new JSONObject();
        obj.put("transactionId", transactionId);
        obj.put("senderWalletId", senderId);
        obj.put("receiverWalletId", receiverId);
        obj.put("amount", amount);
        obj.put("status", status);
        return obj;
    }
}
