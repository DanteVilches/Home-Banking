package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newTransaction(
            @RequestParam Double amount, @RequestParam String description,
            @RequestParam String originAccount, @RequestParam String destinationAccount, Authentication authentication) {

        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Account originAccountFromRepository = accountService.getAccountByNumber(originAccount);
        Account destinationAccountFromRepository = accountService.getAccountByNumber(destinationAccount);

        if (amount.isNaN() || amount == 0 || amount.isInfinite()){
            return new ResponseEntity<>("The amount is incorrect", HttpStatus.FORBIDDEN);
        }

        if (description.isEmpty() ){
            return new ResponseEntity<>("The description field is empty", HttpStatus.FORBIDDEN);
        }
        if (originAccount.isEmpty() ){
            return new ResponseEntity<>("The origin account field is empty", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount.isEmpty() ){
            return new ResponseEntity<>("The destination account field is empty", HttpStatus.FORBIDDEN);
        }
        if(originAccount.equals(destinationAccount)){
            return new ResponseEntity<>("The destination account is the same as the origin account", HttpStatus.FORBIDDEN);
        }

        if (accountService.getAccountByNumber(originAccount) == null){
            return new ResponseEntity<>("The origin account does not exist", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(originAccount)).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("You are not the owner of this account", HttpStatus.FORBIDDEN);
        }
        if (accountService.getAccountByNumber(destinationAccount) == null){
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);

        }
        if (accountService.getAccountByNumber(originAccount).getBalance() < amount){
            return new ResponseEntity<>("Not enough balance to do this transaction", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount,description+" - "+destinationAccount,LocalDateTime.now(),originAccountFromRepository.getBalance()-amount);
        Transaction creditTransaction= new Transaction(TransactionType.CREDIT,amount,description+" - "+originAccount,LocalDateTime.now(),destinationAccountFromRepository.getBalance()+amount);
        originAccountFromRepository.addTransaction(debitTransaction);
        destinationAccountFromRepository.addTransaction(creditTransaction);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        originAccountFromRepository.setBalance(originAccountFromRepository.getBalance() - amount);
        destinationAccountFromRepository.setBalance(destinationAccountFromRepository.getBalance() + amount);

        accountService.saveAccount(originAccountFromRepository);
        accountService.saveAccount(destinationAccountFromRepository);

        return new ResponseEntity<>("The transaction has been made",HttpStatus.CREATED);




    }
}
