package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existClients(){

        List<Client> client = clientRepository.findAll();
        assertThat(client,is(not(empty())));

    }

    @Test
    public void existClientMelba(){

        List<Client> client = clientRepository.findAll();

        assertThat(client, hasItem(hasProperty("firstName", is("Melba"))));

    }

    @Test
    public void existCards(){

        List<Card> card = cardRepository.findAll();
        assertThat(card,is(not(empty())));

    }

    @Test
    public void existTitaniumCards(){

        List<Card> card = cardRepository.findAll();

        assertThat(card, hasItem(hasProperty("color", is(CardColor.TITANIUM))));

    }

    @Test
    public void existAccounts(){

        List<Account> acc = accountRepository.findAll();
        assertThat(acc,is(not(empty())));

    }

    @Test
    public void existAccountWithBalance(){

        List<Account> acc = accountRepository.findAll();

        assertThat(acc, hasItem(hasProperty("balance", is(not(empty())))));

    }

    @Test
    public void existTransactions(){

        List<Transaction> tran = transactionRepository.findAll();
        assertThat(tran,is(not(empty())));

    }

    @Test
    public void existDebitTransactions(){

        List<Transaction> tran = transactionRepository.findAll();

        assertThat(tran, hasItem(hasProperty("type", is(TransactionType.DEBIT))));

    }
}
