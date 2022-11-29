package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;

public class AccountDTO {


    private Long accountId;
    private String accountNumber;
    private LocalDateTime accountCreationDate;
    private Double accountBalance;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.accountId = account.getId();
        this.accountNumber = account.getNumber();
        this.accountCreationDate = account.getCreationDate();
        this.accountBalance = account.getBalance();
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

}
