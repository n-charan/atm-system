package com.coding.service;

import com.coding.model.AccountCard;
import com.coding.model.AccountCardInvalidLog;
import com.coding.repository.AccountCardInvalidLogRepository;
import com.coding.repository.AccountCardRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AuthenticationService {

    private final AccountCardRepository accountCardRepository;
    private final AccountCardInvalidLogRepository accountCardInvalidLogRepository;

    public AuthenticationService(AccountCardRepository accountCardRepository,
                                 AccountCardInvalidLogRepository accountCardInvalidLogRepository) {
        this.accountCardRepository = accountCardRepository;
        this.accountCardInvalidLogRepository = accountCardInvalidLogRepository;
    }

    public AccountCard validateCardNumber(String cardNumber) {
        return accountCardRepository.getAccountCardByCardNumber(cardNumber);
    }

    public Boolean validateCard(AccountCard accountCard) {
        Boolean isValidCard = Boolean.TRUE;
        Boolean isExpired = Boolean.FALSE;
        LocalDate presentDate = LocalDate.now();
        int month = presentDate.getMonth().getValue();
        int year = presentDate.getYear();
        int cardMonth = Integer.parseInt(accountCard.getExpiryMonth());
        int cardYear =  Integer.parseInt(accountCard.getExpiryYear());
        if ((cardYear == year && cardMonth < month) || (cardYear < year)) {
            isExpired = Boolean.TRUE;
        }
        if (isExpired || accountCard.getIsBlocked()) {
            isValidCard = Boolean.FALSE;
        }
        return isValidCard;
    }

    public AccountCard authenticateCard(AccountCard accountCard, String pin) {
        AccountCard selectedAccountCard = null;
        if (accountCard.getPin().equals(pin)) {
            selectedAccountCard = accountCard;
        } else {
            AccountCardInvalidLog accountCardInvalidLog = new AccountCardInvalidLog();
            accountCardInvalidLog.setAccountCard(accountCard);
            accountCardInvalidLog.setWrongPinTime(LocalDateTime.now());
            accountCardInvalidLogRepository.save(accountCardInvalidLog);
            List<AccountCardInvalidLog> logList = accountCardInvalidLogRepository.getAllByAccountCardId(accountCard.getId());
            if (logList.size()%3 == 0) {
                accountCard.setIsBlocked(Boolean.TRUE);
                accountCardRepository.save(accountCard);
            }

        }
        return selectedAccountCard;
    }
}
