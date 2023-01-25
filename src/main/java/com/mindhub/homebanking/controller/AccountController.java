package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
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
import java.util.stream.Collectors;

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
        return accountService.getAllAccounts().stream().filter(account -> account.getEnabled().equals(true)).map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountsById(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));

    }

    @PostMapping("/clients/current/accounts")
    ResponseEntity<Object> createNewAccount(Authentication authentication, @RequestParam AccountType accountType){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient == null){
            return new ResponseEntity<>("You're not authenticated", HttpStatus.FORBIDDEN);
        }


    if(accountType == null){
        return new ResponseEntity<>("Invalid account type", HttpStatus.FORBIDDEN);
    }
        if (currentClient.getAccounts().stream().filter(account -> account.getEnabled().equals(true)).collect(Collectors.toSet()).size() < 3){
            Account newAccount = new Account("VIN-"+ CardUtils.getRandomNumber(10000000,99999999),LocalDateTime.now(),0D,accountType);
            currentClient.addAccount(newAccount);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/clients/current/accounts")
    ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam Long id ){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Account currentAccount = accountService.getAccountById(id);


        if (!currentClient.getAccounts().contains(currentAccount)){
            return new ResponseEntity<>("You're not this account Owner", HttpStatus.FORBIDDEN);
        }
        if (currentAccount == null){
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (currentAccount.getBalance() > 0){
            return new ResponseEntity<>("This account has balance!", HttpStatus.FORBIDDEN);
        }
        if (currentAccount.getEnabled().equals(false)){
            return new ResponseEntity<>("This account is already 'deleted' ", HttpStatus.FORBIDDEN);
        }



        currentAccount.setEnabled(false);
        accountService.saveAccount(currentAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
