package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private Boolean isEnabled;

    private AccountType accountType;
    private Set<TransactionDTO> transactionDTO;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.isEnabled = account.getEnabled();
        this.accountType= account.getAccountType();
        this.transactionDTO = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());

    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Set<TransactionDTO> getTransactionDTO() {
        return transactionDTO;
    }

}
