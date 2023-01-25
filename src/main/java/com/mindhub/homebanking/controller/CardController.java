package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {


    @Autowired
    private ClientService clientService;
    @Autowired
    private CardService cardService;


    @PostMapping("/clients/current/cards")
    ResponseEntity<Object> createNewCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client currentClient = clientService.getClientByEmail(authentication.getName());


        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (!currentClient.getCards().stream().filter(card -> card.getIsEnabled().equals(true)).filter(card -> card.getType().equals(cardType)).filter(card -> card.getColor().equals(cardColor)).collect(Collectors.toSet()).isEmpty()){
        return new ResponseEntity<>("There's already a card of this type/color", HttpStatus.FORBIDDEN);
        }

        Card newCard = new Card(currentClient.getFirstName()+" "+currentClient.getLastName(), cardType,cardColor,CardUtils.createRandomCard() ,CardUtils.generateCVV(),LocalDate.now().plusYears(5), LocalDate.now());
        currentClient.addCard(newCard);
        cardService.saveCard(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
}
@PatchMapping("/clients/current/cards")
    ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam Long id ){
    Client currentClient = clientService.getClientByEmail(authentication.getName());
    Card currentCard = cardService.getCardById(id);


    if (!currentClient.getCards().contains(currentCard)){
    return new ResponseEntity<>("You're not this card Owner", HttpStatus.FORBIDDEN);
    }
    if (currentCard == null){
        return new ResponseEntity<>("This card doesn't exist", HttpStatus.FORBIDDEN);
    }
    if (currentCard.getIsEnabled().equals(false)){
        return new ResponseEntity<>("This card is already 'deleted' ", HttpStatus.FORBIDDEN);
    }



    currentCard.setEnabled(false);
    cardService.saveCard(currentCard);
    return new ResponseEntity<>(HttpStatus.CREATED);
}
}

