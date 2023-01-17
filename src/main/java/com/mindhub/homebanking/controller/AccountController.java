package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountsById(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));

    }

    @PostMapping("/clients/current/accounts")
    ResponseEntity<Object> createNewAccount(Authentication authentication){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient.getAccounts().size() < 3){
            Account newAccount = new Account("VIN-"+ CardUtils.getRandomNumber(10000000,99999999),LocalDateTime.now(),0D);
            currentClient.addAccount(newAccount);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }




}
