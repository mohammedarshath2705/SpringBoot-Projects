package com.ewallet.heropay.transactionservice.controller;

import com.ewallet.heropay.transactionservice.dto.TransactionCreateRequest;
import com.ewallet.heropay.transactionservice.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public String transact(@RequestBody @Valid TransactionCreateRequest request) throws JsonProcessingException {
        return transactionService.transact(request);
    }
}
