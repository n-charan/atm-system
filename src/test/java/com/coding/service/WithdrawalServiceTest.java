package com.coding.service;

import com.coding.dto.DenominationDTO;
import com.coding.model.*;
import com.coding.repository.ATMDenominationRepository;
import com.coding.repository.UserAccountRepository;
import com.coding.repository.UserAccountTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class WithdrawalServiceTest {

    private static ATMDenominationRepository atmDenominationRepository;
    private static UserAccountRepository userAccountRepository;
    private static UserAccountTransactionRepository userAccountTransactionRepository;
    private static WithdrawalService withdrawalService;
    private static User user;
    private static UserAccount userAccount;
    private static AccountCard accountCard;
    private static List<ATMDenomination> atmDenominationList = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        atmDenominationRepository = Mockito.mock(ATMDenominationRepository.class);
        userAccountRepository = Mockito.mock(UserAccountRepository.class);
        userAccountTransactionRepository = Mockito.mock(UserAccountTransactionRepository.class);

        withdrawalService = new WithdrawalService(atmDenominationRepository, userAccountRepository, userAccountTransactionRepository);

        user = new User(1L, "Nishant", "Charan", LocalDate.of(1989, 8, 21));
        userAccount = new UserAccount(1L, user, "43456343", "SAVINGS", 35000, "SBI");
        accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);
        initializeATMDenomination();
    }

    private static void initializeATMDenomination() {
        ATMDenomination atmDenomination1 = new ATMDenomination(4L, 2000, 5);
        ATMDenomination atmDenomination2 = new ATMDenomination(3L, 500, 10);
        ATMDenomination atmDenomination3 = new ATMDenomination(2L, 200, 30);
        ATMDenomination atmDenomination4 = new ATMDenomination(1L, 100, 50);

        atmDenominationList.addAll(Arrays.asList(atmDenomination1, atmDenomination2, atmDenomination3, atmDenomination4));
    }

    @Test
    @DisplayName("Test to check amount if its in multiple of 100")
    public void checkIfMultiplesTest_1() {
        Integer amount = 2500;
        Boolean isValidAmount = withdrawalService.checkIfMultiples(amount);

        Assertions.assertTrue(isValidAmount, "Amount is not in multiple of 100");
    }

    @Test
    @DisplayName("Test to check amount if its not in multiple of 100")
    public void checkIfMultiplesTest_2() {
        Integer amount = 2550;
        Boolean isValidAmount = withdrawalService.checkIfMultiples(amount);

        Assertions.assertFalse(isValidAmount, "Amount is in multiple of 100");
    }

    @Test
    @DisplayName("Test to check ATM withdrawl limit when the amount is valid and ATM and Card are of same bank")
    public void checkATMTransactionLimitTest_1() {
        Integer amount = 20000;
        Boolean isValidAmount = withdrawalService.checkATMTransactionLimit(accountCard, amount);

        Assertions.assertTrue(isValidAmount, "Amount entered is not valid");
    }

    @Test
    @DisplayName("Test to check ATM withdrawl limit when the amount is invalid and ATM and Card are of same bank")
    public void checkATMTransactionLimitTest_2() {
        Integer amount = 30000;
        Boolean isValidAmount = withdrawalService.checkATMTransactionLimit(accountCard, amount);

        Assertions.assertFalse(isValidAmount, "Amount entered is valid");
    }

    @Test
    @DisplayName("Test to check ATM withdrawl limit when the amount is valid and ATM and Card are not of same bank")
    public void checkATMTransactionLimitTest_3() {
        Integer amount = 1000;
        UserAccount userAccount = new UserAccount(2L, user, "43456343", "SAVINGS", 35000, "ICIC");
        AccountCard accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);

        Boolean isValidAmount = withdrawalService.checkATMTransactionLimit(accountCard, amount);

        Assertions.assertTrue(isValidAmount, "Amount entered is not valid");
    }

    @Test
    @DisplayName("Test to check ATM withdrawl limit when the amount is not valid and ATM and Card are not of same bank")
    public void checkATMTransactionLimitTest_4() {
        Integer amount = 6000;
        UserAccount userAccount = new UserAccount(2L, user, "43456343", "SAVINGS", 35000, "ICIC");
        AccountCard accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);

        Boolean isValidAmount = withdrawalService.checkATMTransactionLimit(accountCard, amount);

        Assertions.assertFalse(isValidAmount, "Amount entered is valid");
    }

    @Test
    @DisplayName("Test to check account balance if balance is more than entered amount")
    public void checkAccountBalanceTest_1() {
        Integer amount = 10000;
        Boolean isValidAmount = withdrawalService.checkAccountBalance(accountCard, amount);

        Assertions.assertTrue(isValidAmount, "Amount entered is not valid");
    }

    @Test
    @DisplayName("Test to check account balance if balance is less than entered amount")
    public void checkAccountBalanceTest_2() {
        Integer amount = 15000;
        UserAccount userAccount = new UserAccount(2L, user, "43456343", "SAVINGS", 10000, "SBI");
        AccountCard accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);
        Boolean isValidAmount = withdrawalService.checkAccountBalance(accountCard, amount);

        Assertions.assertFalse(isValidAmount, "Amount entered is valid");
    }

    @Test
    @DisplayName("Test to check ATM balance when ATM has valid amount")
    public void checkATMBalanceTest_1() {
        Integer amount = 10000;
        when(atmDenominationRepository.getAll()).thenReturn(atmDenominationList);
        DenominationDTO denominationDTO = withdrawalService.checkATMBalance(amount);

        Integer denominationAmount = (denominationDTO.getDenomination_2000_count() * 2000)
                + (denominationDTO.getDenomination_500_count() * 500)
                + (denominationDTO.getDenomination_200_count() * 200)
                + (denominationDTO.getDenomination_100_count() * 100);

        Assertions.assertTrue(amount.equals(denominationAmount), "ATM does not have enough amount");
    }

    @Test
    @DisplayName("Test to check ATM balance when ATM does not have valid amount")
    public void checkATMBalanceTest_2() {
        Integer amount = 50000;
        when(atmDenominationRepository.getAll()).thenReturn(atmDenominationList);
        DenominationDTO denominationDTO = withdrawalService.checkATMBalance(amount);

        Integer denominationAmount = (denominationDTO.getDenomination_2000_count() * 2000)
                + (denominationDTO.getDenomination_500_count() * 500)
                + (denominationDTO.getDenomination_200_count() * 200)
                + (denominationDTO.getDenomination_100_count() * 100);

        Assertions.assertFalse(amount.equals(denominationAmount), "ATM have enough amount");
    }

    @Test
    @DisplayName("Test to withdraw amount")
    public void withdrawAmountTest() {
        Double accountBal = userAccount.getAmount();
        Integer amount = 1500;

        DenominationDTO denominationDTO = new DenominationDTO();
        denominationDTO.setDenomination_100_count(1);
        denominationDTO.setDenomination_200_count(2);
        denominationDTO.setDenomination_500_count(2);

        doNothing().when(atmDenominationRepository).updateATMDominations(denominationDTO);
        when(userAccountRepository.getById(accountCard.getUserAccount().getId())).thenReturn(userAccount);
        doNothing().when(userAccountTransactionRepository).save(any(UserAccountTransaction.class));

        UserAccount userAccount1 = userAccount;
        userAccount1.setAmount(accountBal - amount);
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount1);

        Assertions.assertEquals(userAccount1.getId(), userAccount.getId());
        Assertions.assertEquals(accountBal - amount, userAccount1.getAmount());
    }

}