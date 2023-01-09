package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    Loan getLoanById(Long id);
}
