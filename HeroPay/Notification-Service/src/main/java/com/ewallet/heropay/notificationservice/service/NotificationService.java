package com.ewallet.heropay.notificationservice.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final String TRANSACTION_COMPLETED_TOPIC = "transaction_completed";

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = {TRANSACTION_COMPLETED_TOPIC}, groupId = "jbdl123")
    public void notify(String msg) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(msg);

            String transactionStatus = (String) obj.get("transactionStatus");
            String transactionId = (String) obj.get("transactionId");
            Long amount = (Long) obj.get("amount");
            String senderEmail = (String) obj.get("senderEmail");
            String receiverEmail = (String) obj.get("receiverEmail");

            String senderMsg = getSenderMessage(transactionStatus, amount, transactionId);
            String receiverMsg = getReceiverMessage(transactionStatus, amount, senderEmail);

            sendEmail(senderEmail, senderMsg);
            sendEmail(receiverEmail, receiverMsg);

        } catch (ParseException e) {
            // Log and handle the error
            System.err.println("Error parsing JSON message: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String message) {
        if (to != null && !to.isEmpty() && message != null && !message.isEmpty()) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(to);
            email.setSubject("E-Wallet Transaction Updates");
            email.setFrom("youremail@gmail.com"); // Replace with your actual email address
            email.setText(message);
            javaMailSender.send(email);
        }
    }

    private String getSenderMessage(String transactionStatus, Long amount, String transactionId) {
        if ("FAILED".equalsIgnoreCase(transactionStatus)) {
            return "Hi! Your transaction of amount " + amount + " with transaction ID " + transactionId + " has failed.";
        } else {
            return "Hi! Your account has been debited with amount " + amount + " for transaction ID " + transactionId + ".";
        }
    }

    private String getReceiverMessage(String transactionStatus, Long amount, String senderEmail) {
        if ("SUCCESSFUL".equalsIgnoreCase(transactionStatus)) {
            return "Hi! Your account has been credited with amount " + amount + " for the transaction done by user " + senderEmail + ".";
        }
        return null;
    }
}
