package com.coding.service;

import com.coding.model.AccountCard;
import com.coding.model.User;
import com.coding.model.UserAccount;
import com.coding.model.UserAccountTransaction;
import com.coding.repository.AccountCardRepository;
import com.coding.repository.UserAccountTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private static UserAccountTransactionRepository userAccountTransactionRepository;
    private static AccountCardRepository accountCardRepository;
    private static AccountService accountService;
    private static User user;
    private static UserAccount userAccount;
    private static AccountCard accountCard;

    @BeforeAll
    public static void setup() {
        userAccountTransactionRepository = Mockito.mock(UserAccountTransactionRepository.class);
        accountCardRepository = Mockito.mock(AccountCardRepository.class);
        accountService = new AccountService(userAccountTransactionRepository, accountCardRepository);
        user = new User(1L, "Nishant", "Charan", LocalDate.of(1989, 8, 21));
        userAccount = new UserAccount(1L, user, "43456343", "SAVINGS", 35000, "SBI");
        accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);
    }

    @Test
    @DisplayName("Test change pin when old pin and new pin is same")
    public void changePinTest_1() {
        String newPin = "1234";
        String confirmPin = "1234";
        String pinChangeMsg = accountService.changePin(accountCard, newPin, confirmPin);

        Assertions.assertEquals("New pin cannot be same as new pin", pinChangeMsg);
    }

    @Test
    @DisplayName("Test change pin when old pin and new pin is not same but confirm pin is different")
    public void changePinTest_2() {
        String newPin = "4356";
        String confirmPin = "4357";
        String pinChangeMsg = accountService.changePin(accountCard, newPin, confirmPin);

        Assertions.assertEquals("Confirm Pin is not matching", pinChangeMsg);
    }

    @Test
    @DisplayName("Test change pin when old pin and new pin is not same ")
    public void changePinTest_3() {
        String newPin = "4356";
        String confirmPin = "4356";
        String pinChangeMsg = accountService.changePin(accountCard, newPin, confirmPin);

        Assertions.assertEquals("Pin change successful", pinChangeMsg);
    }

    @Test
    @DisplayName("Test to get account balance")
    public void getAccountBalanceTest() {
        Double accountBalance = accountService.getAccountBalance(accountCard);
        Assertions.assertEquals(35000, accountBalance, "Amount is different");
    }

    @Test
    @DisplayName("Test to get mini statement")
    public void getMiniStatementTest() {
        UserAccountTransaction uat1 = new UserAccountTransaction(1L, userAccount, 1200, "DEBIT", LocalDateTime.now());
        UserAccountTransaction uat2 = new UserAccountTransaction(1L, userAccount, 2000, "CREDIT", LocalDateTime.now());
        UserAccountTransaction uat3 = new UserAccountTransaction(1L, userAccount, 1000, "DEBIT", LocalDateTime.now());
        UserAccountTransaction uat4 = new UserAccountTransaction(1L, userAccount, 1500, "CREDIT", LocalDateTime.now());
        UserAccountTransaction uat5 = new UserAccountTransaction(1L, userAccount, 2000, "DEBIT", LocalDateTime.now());
        List<UserAccountTransaction> transactionList = new ArrayList<>(Arrays.asList(uat1,uat2,uat3,uat4,uat5));
        when(userAccountTransactionRepository.getTransactionsByAccountId(userAccount.getId())).thenReturn(transactionList);

        List<UserAccountTransaction> userAccountTransactionList = accountService.getMiniStatement(accountCard);

        Assertions.assertEquals(transactionList.size(), userAccountTransactionList.size());
        Assertions.assertEquals(3, userAccountTransactionList.stream().filter(uat -> uat.getTransactionType().equals("DEBIT")).count());
    }
}
