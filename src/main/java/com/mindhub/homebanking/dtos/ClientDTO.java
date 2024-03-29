package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<ClientLoanDTO> loans;
    private Set<AccountDTO> accountDTO;

    private Set<CardDTO> cardDTO;

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.loans = client.getClientLoans().stream().map(loans -> new ClientLoanDTO(loans)).collect(Collectors.toSet());
        this.accountDTO = client.getAccounts().stream().filter(account -> account.getEnabled().equals(true)).map(accounts -> new AccountDTO(accounts)).collect(Collectors.toSet());
        this.cardDTO = client.getCards().stream().filter(card -> card.getIsEnabled().equals(true)).map(cards -> new CardDTO(cards)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<AccountDTO> getAccountDTO() {
        return accountDTO;
    }

    public Set<CardDTO> getCardDTO() {
        return cardDTO;
    }
}