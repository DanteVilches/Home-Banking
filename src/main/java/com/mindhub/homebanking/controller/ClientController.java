package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
}
