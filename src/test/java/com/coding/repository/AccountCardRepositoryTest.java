package com.coding.repository;

import com.coding.model.AccountCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountCardRepositoryTest {

    private static AccountCardRepository accountCardRepository;

    @BeforeAll
    public static void setup() {
        accountCardRepository = new AccountCardRepository();
    }

    @Test
    @DisplayName("Test to check if card exists with account")
    public void getAccountCardByCardNumberTest() {
        String cardNumber = "2345123487568764";
        AccountCard accountCard = accountCardRepository.getAccountCardByCardNumber(cardNumber);

        Assertions.assertNotNull(accountCard);
    }

}
