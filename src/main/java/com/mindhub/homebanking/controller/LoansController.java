package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoansController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;


    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }


    @Transactional
    @PostMapping("/clients")
    public ResponseEntity<Object> loanApplication(@RequestBody LoanApplicationDTO loan, Authentication authentication) {

        Client currentClient = clientRepository.findByEmail(authentication.getName());

        Loan currentLoan = loanRepository.findById(loan.getId()).orElse(null);

        Account currentAccount = accountRepository.findByNumber(loan.getAccount());
        if (currentLoan == null){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(loan.getId() == null){
            return new ResponseEntity<>("Loan id can't be empty.", HttpStatus.FORBIDDEN);
        }
        if (loan.getPayments() == null){
            return new ResponseEntity<>("Payments are incorrect", HttpStatus.FORBIDDEN);
        }
        if( currentLoan.getPayments().stream().filter(payment -> payment.equals(loan.getPayments())).collect(Collectors.toSet()).isEmpty() ){
            return new ResponseEntity<>("Payment can't be found", HttpStatus.FORBIDDEN);

        }
        if(loan.getAmount() < 0 || loan.getAmount() == 0 ){
            return new ResponseEntity<>("Amount can't be lower or equal to 0", HttpStatus.FORBIDDEN);
        }
        if(loan.getAmount() > currentLoan.getMaxAmount()){
            return new ResponseEntity<>("Amount can't be higher than the max amount", HttpStatus.FORBIDDEN);
        }
        if(loan.getAccount() == null){
            return new ResponseEntity<>("The account field can't be empty", HttpStatus.FORBIDDEN);

        }
        if(currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(loan.getAccount())).collect(Collectors.toSet()).isEmpty()){

            return new ResponseEntity<>("You're not the owner of this account", HttpStatus.FORBIDDEN);
        }
        ClientLoan newClientLoan = new ClientLoan(loan.getAmount()*1.20,loan.getPayments(), LocalDate.now());
        Transaction newTransaction = new Transaction(TransactionType.CREDIT,loan.getAmount(), currentLoan.getName()+"Loan approved",LocalDateTime.now());
        currentAccount.setBalance(currentAccount.getBalance()+loan.getAmount());

        currentAccount.addTransaction(newTransaction);
        currentClient.addClientLoan(newClientLoan);
        currentLoan.addClientLoan(newClientLoan);

        clientRepository.save(currentClient);
        loanRepository.save(currentLoan);
        accountRepository.save(currentAccount);
        clientLoanRepository.save(newClientLoan);
        transactionRepository.save(newTransaction);

        return new ResponseEntity<>("Loan has been successfully acquired ",HttpStatus.CREATED);
    }
}
