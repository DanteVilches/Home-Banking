package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {


    private Long accountId;
    private String accountNumber;
    private LocalDateTime accountCreationDate;
    private Double accountBalance;
    private Set<TransactionDTO> transactionDTO;
    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.accountId = account.getId();
        this.accountNumber = account.getNumber();
        this.accountCreationDate = account.getCreationDate();
        this.accountBalance = account.getBalance();
        this.transactionDTO = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());

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

    public Set<TransactionDTO> getTransactionDTO() {
        return transactionDTO;
    }

}
