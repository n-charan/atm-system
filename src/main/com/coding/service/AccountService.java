package com.coding.service;

import com.coding.model.AccountCard;
import com.coding.model.UserAccountTransaction;
import main.java.com.coding.repository.AccountCardRepository;
import main.repository.UserAccountTransactionRepository;

import java.util.List;

public class AccountService {

    private final UserAccountTransactionRepository userAccountTransactionRepository;
    private final AccountCardRepository accountCardRepository;

    public AccountService(UserAccountTransactionRepository userAccountTransactionRepository,
                   AccountCardRepository accountCardRepository) {
        this.userAccountTransactionRepository = userAccountTransactionRepository;
        this.accountCardRepository = accountCardRepository;
    }

    public String changePin(AccountCard accountCard, String newPin, String confirmPin) {
        String changePin = "";
        if (!newPin.equals(confirmPin)) {
            changePin = "Confirm Pin is not matching";
        } else if (accountCard.getPin().equals(newPin)) {
            changePin = "New pin cannot be same as new pin";
        } else {
            accountCard.setPin(newPin);
            accountCardRepository.save(accountCard);
            changePin = "Pin change successful";
        }
        return changePin;
    }

    public Double getAccountBalance(AccountCard accountCard) {
        return accountCard.getUserAccount().getAmount();
    }

    public List<UserAccountTransaction> getMiniStatement(AccountCard accountCard) {
        return userAccountTransactionRepository.getTransactionsByAccountId(accountCard.getUserAccount().getId());
    }
}
