package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Client;


import java.util.Set;
import java.util.stream.Collectors;



public class ClientDTO{

    private Long idClient;
    private String clientName;
    private String clientLastName;
    private String clientEmail;

    private Set<AccountDTO> accountDTO;

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        this.idClient = client.getIdClient();
        this.clientName = client.getClientName();
        this.clientLastName = client.getClientLastName();
        this.clientEmail = client.getClientEmail();
        this.accountDTO = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());


    }


    public Long getIdClient() {
        return idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public Set<AccountDTO> getAccountDTO() {
        return accountDTO;
    }
}