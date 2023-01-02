package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> newTransaction(
            @RequestParam Double amount, @RequestParam String description,
            @RequestParam String originAccount, @RequestParam String destinationAccount, Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Account originAccountFromRepository = accountRepository.findByNumber(originAccount);
        Account destinationAccountFromRepository = accountRepository.findByNumber(destinationAccount);

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

        if (accountRepository.findByNumber(originAccount) == null){
            return new ResponseEntity<>("The origin account does not exist", HttpStatus.FORBIDDEN);
        }

        if (currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(originAccount)).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("You are not the owner of this account", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(destinationAccount) == null){
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);

        }
        if (accountRepository.findByNumber(originAccount).getBalance() < amount){
            return new ResponseEntity<>("Not enough balance to do this transaction", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount,description+" "+destinationAccount,LocalDateTime.now());
        Transaction creditTransaction= new Transaction(TransactionType.CREDIT,amount,description+" "+originAccount,LocalDateTime.now());
        originAccountFromRepository.addTransaction(debitTransaction);
        destinationAccountFromRepository.addTransaction(creditTransaction);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        originAccountFromRepository.setBalance(originAccountFromRepository.getBalance() - amount);
        destinationAccountFromRepository.setBalance(destinationAccountFromRepository.getBalance() + amount);

        accountRepository.save(originAccountFromRepository);
        accountRepository.save(destinationAccountFromRepository);

        return new ResponseEntity<>("The transaction has been made",HttpStatus.CREATED);




    }
}
