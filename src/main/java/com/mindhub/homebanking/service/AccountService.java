package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    void saveAccount(Account account);

    Account getAccountByNumber(String number);
}
