package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){

        String cardNumber = CardUtils.createRandomCard();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }
    @Test
    public void cardCVVIsCreated(){

        int cardCVV= CardUtils.generateCVV();

        assertThat(cardCVV,is(not(nullValue())));

    }

    @Test
    public void cardCVVIsValid(){

        int cardCVV= CardUtils.generateCVV();

        assertThat(cardCVV,is(not(greaterThan(999))));

    }
}
