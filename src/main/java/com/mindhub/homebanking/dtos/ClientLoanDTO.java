package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.time.LocalDate;
import java.util.Date;

public class ClientLoanDTO {
    private Long id;

    private Long loanId;

    private String name;

    private Double amount;

    private Integer payment;

    private LocalDate date;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan loans) {
        this.loanId = loans.getLoan().getId();
        this.name = loans.getLoan().getName();
        this.id = loans.getId();
        this.amount = loans.getAmount();
        this.payment = loans.getPayment();
        this.date = loans.getDate();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public LocalDate getDate() {
        return date;
    }
}
