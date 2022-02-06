package com.coding;

import com.coding.dto.DenominationDTO;
import com.coding.model.AccountCard;
import com.coding.model.UserAccountTransaction;
import com.coding.repository.*;
import com.coding.service.AccountService;
import com.coding.service.AuthenticationService;
import com.coding.service.WithdrawalService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ATM_Main {

    private static AuthenticationService authenticationService;
    private static AccountService accountService;
    private static WithdrawalService withdrawalService;

    private static void initializeObjects() {
        AccountCardRepository accountCardRepository = new AccountCardRepository();
        UserAccountTransactionRepository userAccountTransactionRepository = new UserAccountTransactionRepository();
        ATMDenominationRepository atmDenominationRepository = new ATMDenominationRepository();
        UserAccountRepository userAccountRepository = new UserAccountRepository();
        AccountCardInvalidLogRepository accountCardInvalidLogRepository = new AccountCardInvalidLogRepository();
        authenticationService = new AuthenticationService(accountCardRepository, accountCardInvalidLogRepository);
        accountService = new AccountService(userAccountTransactionRepository, accountCardRepository);
        withdrawalService = new WithdrawalService(atmDenominationRepository, userAccountRepository, userAccountTransactionRepository);
    }

    public static void main (String [] args) {
        initializeObjects();
        System.out.println("Welcome to SBI ATM");
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your card number(16 digits) : ");
        String cardNumber = sc.next();
        if (cardNumber.length() != 16) {
            System.out.println("Invalid Card Number. Please try again!!");
        }
        AccountCard accountCard = authenticationService.validateCardNumber(cardNumber);
        if (accountCard == null) {
            System.out.println("Invalid Card. Please try again!!");
            System.exit(0);
        }
        validateAccount(accountCard, sc);
    }

    private static void validateAccount(AccountCard accountCard, Scanner scanner) {
        System.out.print("Please enter your 4-digit PIN : ");
        String pin = scanner.next();
        if (!authenticationService.validateCard(accountCard)) {
            System.out.println("The card has been expired or blocked. Please visit nearest branch!!");
            System.exit(0);
        }
        AccountCard returnedAccountCard = authenticationService.authenticateCard(accountCard, pin);
        if (returnedAccountCard == null) {
            System.out.println("You have entered wrong pin. Please try again!!");
            validateAccount(accountCard, scanner);
        } else {
            atmTransactions(returnedAccountCard);
        }
    }

    private static void checkToContinue(AccountCard accountCard, Scanner scanner) {
        System.out.print("Do you want to continue(Y/N) : ");
        String flag = scanner.next();
        if (flag.equals("Y")) {
            validateAccount(accountCard, scanner);
        } else {
            System.exit(0);
        }
    }

    private static void atmTransactions(AccountCard accountCard) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome " + accountCard.getUserAccount().getUser().getFirstName() + "!");
        System.out.println("1. Change PIN");
        System.out.println("2. Check Balance");
        System.out.println("3. Mini Statement");
        System.out.println("4. Withdrawal");
        System.out.println("5. Exit");
        System.out.print("Please enter your choice : ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1 : changePinLogic(sc, accountCard);
                        break;
            case 2 : checkBalanceLogic(sc, accountCard);
                        break;
            case 3 : miniStatementLogic(sc, accountCard);
                        break;
            case 4 : withdrawal(sc, accountCard);
                        break;
            case 5 : System.exit(0);
                        break;
            default: System.out.println("Wrong Input! Try again!!");
                        break;
        }
    }

    private static void changePinLogic(Scanner scanner, AccountCard accountCard) {
        System.out.print("Enter new pin : ");
        String newPin = scanner.next();
        System.out.print("Confirm new pin : ");
        String confirmPin = scanner.next();
        String pinChangeSuccess = accountService.changePin(accountCard, newPin, confirmPin);
        System.out.println(pinChangeSuccess);
        checkToContinue(accountCard, scanner);
    }

    private static void checkBalanceLogic(Scanner scanner, AccountCard accountCard) {
        Double accountBalance = accountService.getAccountBalance(accountCard);
        System.out.println("Account Balance :: " + accountBalance);
        checkToContinue(accountCard, scanner);
    }

    private static void miniStatementLogic(Scanner scanner, AccountCard accountCard) {
        List<UserAccountTransaction> transactions = accountService.getMiniStatement(accountCard);
        System.out.println("Account Transactions ");
        System.out.println("Date\tTransaction Type\tAmount ");
        for (UserAccountTransaction userAccountTransaction : transactions) {
            String date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(userAccountTransaction.getTransactionTime());
            System.out.println(date + "\t"+ userAccountTransaction.getTransactionType() +"\t" + userAccountTransaction.getAmount());
        }
        checkToContinue(accountCard, scanner);
    }

    private static void withdrawal(Scanner scanner, AccountCard accountCard) {
        System.out.print("Enter amount to be withdrawn (multiple of 100) : ");
        Integer amount = scanner.nextInt();
        Boolean isMultiple = withdrawalService.checkIfMultiples(amount);
        if (!isMultiple) {
            System.out.println("Amount should be in multiple of 100.");
            System.exit(0);
        } else if (!withdrawalService.checkATMTransactionLimit(accountCard, amount)){
            System.out.println("Entered amount is out of limit.");
            System.exit(0);
        } else if (!withdrawalService.checkAccountBalance(accountCard, amount)) {
            System.out.println("Insufficient balance in account.");
            System.exit(0);
        }
        DenominationDTO denominationDTO = withdrawalService.checkATMBalance(amount);
        Integer denominationAmount = (denominationDTO.getDenomination_2000_count() * 2000)
                + (denominationDTO.getDenomination_500_count() * 500)
                + (denominationDTO.getDenomination_200_count() * 200)
                + (denominationDTO.getDenomination_100_count() * 100);
        if (amount == denominationAmount) {
            System.out.println("ATM does not have sufficient denominations");
        } else {
            withdrawalService.withdrawAmount(accountCard, denominationDTO, amount);
            System.out.println("Please collect your cash");
            System.out.println("Account Balance : " + accountService.getAccountBalance(accountCard));
            checkToContinue(accountCard, scanner);
        }

    }
}
