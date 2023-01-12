package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoansController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }


    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanApplication(@RequestBody LoanApplicationDTO loan, Authentication authentication) {

        Client currentClient = clientService.getClientByEmail(authentication.getName());

        Loan currentLoan =    loanService.getLoanById(loan.getId());

        Account currentAccount = accountService.getAccountByNumber(loan.getAccount());
        if (currentLoan == null){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(loan.getId() == null){
            return new ResponseEntity<>("Loan id can't be empty.", HttpStatus.FORBIDDEN);
        }
        if (!currentClient.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equals(currentLoan.getName())).collect(Collectors.toSet()).isEmpty()){
            return new ResponseEntity<>("You already own a loan of this type", HttpStatus.FORBIDDEN);
        }
        if (loan.getPayments() == null){
            return new ResponseEntity<>("Payments are incorrect", HttpStatus.FORBIDDEN);
        }
        if( currentLoan.getPayments().stream().filter(payment -> payment.equals(loan.getPayments())).collect(Collectors.toSet()).isEmpty() ){
            return new ResponseEntity<>("This payment is not available for this loan", HttpStatus.FORBIDDEN);

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
        Transaction newTransaction = new Transaction(TransactionType.CREDIT,loan.getAmount(), currentLoan.getName()+" "+"Loan approved",LocalDateTime.now());
        currentAccount.setBalance(currentAccount.getBalance()+loan.getAmount());

        currentAccount.addTransaction(newTransaction);
        currentClient.addClientLoan(newClientLoan);
        currentLoan.addClientLoan(newClientLoan);

    /*   clientService.saveClient(currentClient);
        loanRepository.save(currentLoan);
        accountRepository.save(currentAccount);*/
        clientLoanService.saveClientLoan(newClientLoan);
        transactionService.saveTransaction(newTransaction);

        return new ResponseEntity<>("Loan has been successfully acquired ",HttpStatus.CREATED);
    }
}
