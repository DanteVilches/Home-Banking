package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long id;
    private Double amount;
    private Integer payments;
    private String account;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long id, Double amount, Integer payments, String account) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccount() {
        return account;
    }
}
