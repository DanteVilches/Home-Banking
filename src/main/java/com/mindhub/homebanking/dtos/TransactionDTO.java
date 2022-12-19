package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;


public class TransactionDTO {
    private Long transactionID;

    private TransactionType transactionType;

    private Double transactionAmount;

    private String transactionDescription;

    private LocalDateTime transactionDate;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.transactionID = transaction.getId();
        this.transactionType = transaction.getType();
        this.transactionAmount = transaction.getAmount();
        this.transactionDescription = transaction.getDescription();
        this.transactionDate = transaction.getDate();


    }

    public Long getTransactionID() {
        return transactionID;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}

