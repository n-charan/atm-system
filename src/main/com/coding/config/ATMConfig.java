package com.coding.config;

import com.coding.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ATMConfig {

    public static final String ATM_BANK_CODE = "SBI";

    public static List<ATMDenomination> atmDenominations = new ArrayList<>();
    public static final List<User> userList = new ArrayList<>();
    public static final List<UserAccount> userAccountList = new ArrayList<>();
    public static final List<AccountCard> accountCardList = new ArrayList<>();
    public static final List<UserAccountTransaction> transactionList = new ArrayList<>();

    static {
        initializeATMDenominations();
        initializeUsers();
        initializeUserAccounts();
        initializeAccountCards();
        initializeAccountTransactions();
    }

    public static List<ATMDenomination> getAtmDenominations() {
        return new ArrayList<>(atmDenominations);
    }

    public static List<User> getUserList() {
        return new ArrayList<>(userList);
    }

    public static List<UserAccount> getUserAccountList() {
        return new ArrayList<>(userAccountList);
    }

    public static List<AccountCard> getAccountCardList() {
        return new ArrayList<>(accountCardList);
    }

    public static List<UserAccountTransaction> getTransactionList() {
        return new ArrayList<>(transactionList);
    }

    public static void initializeATMDenominations() {
        ATMDenomination atmDenomination1 = new ATMDenomination(4L, 2000, 5);
        ATMDenomination atmDenomination2 = new ATMDenomination(3L, 500, 10);
        ATMDenomination atmDenomination3 = new ATMDenomination(2L, 200, 30);
        ATMDenomination atmDenomination4 = new ATMDenomination(1L, 100, 50);

        atmDenominations.addAll(Arrays.asList(atmDenomination1, atmDenomination2, atmDenomination3, atmDenomination4));
    }

    public static void initializeUsers() {
        User user1 = new User(1L, "Nishant", "Charan", LocalDate.of(1989, 8, 21));
        User user2 = new User(2L, "Bibhakar", "Prakash", LocalDate.of(1986, 10, 18));
        userList.addAll(new ArrayList<>(Arrays.asList(user1, user2)));
    }

    public static void initializeUserAccounts() {
        UserAccount userAccount1 = new UserAccount(1L, userList.get(0), "2341234", "SAVINGS", 35000, "SBI");
        UserAccount userAccount2 = new UserAccount(2L, userList.get(1), "2335534", "SAVINGS", 5000, "ICIC");
        userAccountList.addAll(new ArrayList<>(Arrays.asList(userAccount1, userAccount2)));
    }

    public static void initializeAccountCards() {
        AccountCard accountCard1 = new AccountCard(1L, userAccountList.get(0), "2345123487568764", "08", "2024", "4563", Boolean.FALSE);
        AccountCard accountCard2 = new AccountCard(1L, userAccountList.get(0), "2345123487568345", "01", "2022", "1123", Boolean.TRUE);
        AccountCard accountCard3 = new AccountCard(1L, userAccountList.get(1), "2345123487569846", "05", "2024", "7643", Boolean.FALSE);
        AccountCard accountCard4 = new AccountCard(1L, userAccountList.get(1), "2345123487569821", "10", "2023", "9867", Boolean.TRUE);

        accountCardList.addAll(new ArrayList<>(Arrays.asList(accountCard1, accountCard2, accountCard3, accountCard4)));
    }

    public static void initializeAccountTransactions() {
        UserAccountTransaction accountTransaction1 = new UserAccountTransaction(1L, userAccountList.get(0), 2500, "CREDIT", LocalDateTime.of(2022, 02, 01, 12, 30));
        UserAccountTransaction accountTransaction2 = new UserAccountTransaction(2L, userAccountList.get(0), 2000, "DEBIT", LocalDateTime.of(2022, 02, 01, 14, 30));
        UserAccountTransaction accountTransaction3 = new UserAccountTransaction(3L, userAccountList.get(0), 1800, "DEBIT", LocalDateTime.of(2022, 02, 01, 18, 15));
        UserAccountTransaction accountTransaction4 = new UserAccountTransaction(4L, userAccountList.get(0), 2000, "CREDIT", LocalDateTime.of(2022, 02, 02, 12, 30));
        UserAccountTransaction accountTransaction5 = new UserAccountTransaction(5L, userAccountList.get(0), 10000, "DEBIT", LocalDateTime.of(2022, 02, 03, 18, 00));
        UserAccountTransaction accountTransaction6 = new UserAccountTransaction(6L, userAccountList.get(0), 2500, "CREDIT", LocalDateTime.of(2022, 02, 05, 19, 30));
        UserAccountTransaction accountTransaction7 = new UserAccountTransaction(7L, userAccountList.get(1), 1800, "DEBIT", LocalDateTime.of(2022, 02, 06, 12, 30));

        transactionList.addAll(new ArrayList<>(Arrays.asList(accountTransaction1, accountTransaction2, accountTransaction3,
                accountTransaction4, accountTransaction5, accountTransaction6, accountTransaction7)));
    }
}
