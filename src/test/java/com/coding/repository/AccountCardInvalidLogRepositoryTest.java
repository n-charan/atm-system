package com.coding.repository;

import com.coding.model.AccountCard;
import com.coding.model.AccountCardInvalidLog;
import com.coding.model.User;
import com.coding.model.UserAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AccountCardInvalidLogRepositoryTest {

    private static AccountCardInvalidLogRepository accountCardInvalidLogRepository;
    private static User user;
    private static UserAccount userAccount;
    private static AccountCard accountCard;

    @BeforeAll
    public static void setup() {
        accountCardInvalidLogRepository = new AccountCardInvalidLogRepository();
        user = new User(1L, "Nishant", "Charan", LocalDate.of(1989, 8, 21));
        userAccount = new UserAccount(1L, user, "43456343", "SAVINGS", 35000, "SBI");
        accountCard = new AccountCard(123L, userAccount, "1234567890121234", "08", "2024", "1234", Boolean.FALSE);
    }

    @Test
    @DisplayName("Test to save account log")
    public void saveTest() {
        AccountCard accountCard = new AccountCard(125L, userAccount, "1234567834121234", "08", "2024", "1234", Boolean.FALSE);
        AccountCardInvalidLog accountCardInvalidLog = new AccountCardInvalidLog();
        accountCardInvalidLog.setAccountCard(accountCard);
        accountCardInvalidLog.setWrongPinTime(LocalDateTime.now());

        AccountCardInvalidLog savedLog = accountCardInvalidLogRepository.save(accountCardInvalidLog);

        Assertions.assertNotNull(savedLog.getId());
    }

    @Test
    @DisplayName("Test to return all account log by account card id")
    public void getAllByAccountCardIdTest() {
        AccountCardInvalidLog accountCardInvalidLog1 = new AccountCardInvalidLog(1L, accountCard, LocalDateTime.now());
        accountCardInvalidLogRepository.save(accountCardInvalidLog1);
        AccountCardInvalidLog accountCardInvalidLog2 = new AccountCardInvalidLog(2L, accountCard, LocalDateTime.now());
        accountCardInvalidLogRepository.save(accountCardInvalidLog2);

        List<AccountCardInvalidLog> logList = accountCardInvalidLogRepository.getAllByAccountCardId(accountCard.getId());
        Assertions.assertEquals(2, logList.size());
    }


}
