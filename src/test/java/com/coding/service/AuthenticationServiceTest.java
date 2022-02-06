package com.coding.service;

import com.coding.model.AccountCard;
import com.coding.model.User;
import com.coding.model.UserAccount;
import com.coding.repository.AccountCardInvalidLogRepository;
import com.coding.repository.AccountCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private static AccountCardRepository accountCardRepository;
    private static AccountCardInvalidLogRepository accountCardInvalidLogRepository;
    private static AuthenticationService authenticationService;
    private static User user;
    private static UserAccount userAccount;

    @BeforeAll
    public static void setup() {
        accountCardRepository = Mockito.mock(AccountCardRepository.class);
        accountCardInvalidLogRepository = Mockito.mock(AccountCardInvalidLogRepository.class);
        authenticationService = new AuthenticationService(accountCardRepository, accountCardInvalidLogRepository);
        user = new User(1L, "Nishant", "Charan", LocalDate.of(1989, 8, 21));
        userAccount = new UserAccount(1L, user, "43456343", "SAVINGS", 35000, "SBI");
    }

    @Test
    @DisplayName("Test to validate card with invalid card number")
    public void validateCardNumberTest_1() {
        String cardNumber = "1234567890121234";
        when(accountCardRepository.getAccountCardByCardNumber(cardNumber)).thenReturn(null);
        AccountCard accountCard = authenticationService.validateCardNumber(cardNumber);

        Assertions.assertEquals(null, accountCard);
    }

    @Test
    @DisplayName("Test to validate card with valid card number")
    public void validateCardNumberTest_2() {
        AccountCard accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);
        String cardNumber = "1234567890121234";
        when(accountCardRepository.getAccountCardByCardNumber(cardNumber)).thenReturn(accountCard);
        AccountCard returnedAccountCard = authenticationService.validateCardNumber(cardNumber);

        Assertions.assertEquals(123L, returnedAccountCard.getId());
    }

    @Test
    @DisplayName("Test card for expiry for expired cards")
    public void checkForExpiryTest_1() {
        AccountCard expiredAccountCard = new AccountCard(120L, userAccount, "1234567890121234", "08", "2021", "1234", Boolean.FALSE);

        Boolean isValidCard = authenticationService.validateCard(expiredAccountCard);

        Assertions.assertFalse(isValidCard, "Card is not expired");
    }

    @Test
    @DisplayName("Test card for expiry for blocked card")
    public void checkForExpiryTest_2() {
        AccountCard blockedAccountCard = new AccountCard(123L, userAccount, "1234567890121234", "10", "2022", "1234", Boolean.TRUE);

        Boolean isValidCard = authenticationService.validateCard(blockedAccountCard);

        Assertions.assertFalse(isValidCard, "Card is not blocked");
    }

    @Test
    @DisplayName("Test card for expiry for non-expired card and non-blocked card")
    public void checkForExpiryTest_3() {
        AccountCard validAccountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);

        Boolean isValidCard = authenticationService.validateCard(validAccountCard);

        Assertions.assertTrue(isValidCard, "Card is either blocked or expired");
    }

    @Test
    @DisplayName("Test card authentication with valid pin")
    public void authenticateCardTest_1() {
        String pin = "1234";
        AccountCard validAccountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);

        AccountCard accountCard = authenticationService.authenticateCard(validAccountCard, pin);

        Assertions.assertEquals(123L, accountCard.getId());
    }

    @Test
    @DisplayName("Test card authentication with invalid pin")
    public void authenticateCardTest_2() {
        String pin = "2345";
        AccountCard validAccountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);

        AccountCard accountCard = authenticationService.authenticateCard(validAccountCard, pin);

        Assertions.assertEquals(null, accountCard);
    }
}
