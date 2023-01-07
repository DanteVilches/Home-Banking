package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private ClientService clientService;


    @Autowired
    private  AccountService accountService;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getAllClients().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClientByID(@PathVariable Long id){
        return  new ClientDTO(clientService.getClientById(id));

    }

    public int  getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min))+min);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password, Authentication authentication) {

        if (firstName.isEmpty() ){
                return new ResponseEntity<>("The first name field is empty", HttpStatus.FORBIDDEN);
        }

        if (lastName.isEmpty() ){
            return new ResponseEntity<>("The last name field is empty", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty() ){
            return new ResponseEntity<>("The email field is empty", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty() ){
            return new ResponseEntity<>("The password field is empty", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        Account newAccount = new Account("VIN-"+getRandomNumber(10000000,99999999),LocalDateTime.now(),0D);
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>("The client has been created",HttpStatus.CREATED);




    }


    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientService.getClientByEmail(authentication.getName()));
    }


}
