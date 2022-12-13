package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;


    private Set<ClientLoanDTO> clientLoanDTO;

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.clientLoanDTO = loan.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }


    public Set<ClientLoanDTO> getClientLoanDTO(){
        return clientLoanDTO;
    }
}
