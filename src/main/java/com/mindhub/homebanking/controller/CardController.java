package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;
    public Integer  getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max-min))+min);
    }
    public String createRandomCard(){
        return getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString()+"-"+getRandomNumber(1000,9999).toString();
    }

    @PostMapping("/clients/current/cards")
    ResponseEntity<Object> createNewCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client currentClient = clientRepository.findByEmail(authentication.getName());


        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (!currentClient.getCards().stream().filter(card -> card.getType().equals(cardType)).filter(card -> card.getColor().equals(cardColor)).collect(Collectors.toSet()).isEmpty()){
        return new ResponseEntity<>("There's already a card of this type/color", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(currentClient.getFirstName()+" "+currentClient.getLastName(), cardType,cardColor,createRandomCard(),getRandomNumber(100,999),LocalDate.now().plusYears(5), LocalDate.now());
        currentClient.addCard(newCard);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
}
}

