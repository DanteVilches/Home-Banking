package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.HashSet;

import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id_client")
    private Long idClient;

    @Column(name= "client_name")
    private String clientName;
    @Column(name= "client_last_name")
    private String clientLastName;

    @Column(name= "client_email")
    private String clientEmail;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    public Client() {
    }
    public Client( String clientName, String clientLastName, String clientEmail) {
        this.clientName = clientName;
        this.clientLastName = clientLastName;
        this.clientEmail = clientEmail;
    }
    public Long getIdClient() {
        return idClient;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }


    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }



}