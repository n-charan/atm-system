package com.coding.service;

import com.coding.config.ATMConfig;
import com.coding.dto.DenominationDTO;
import com.coding.model.ATMDenomination;
import com.coding.model.AccountCard;
import com.coding.model.UserAccount;
import com.coding.model.UserAccountTransaction;
import com.coding.repository.ATMDenominationRepository;
import com.coding.repository.UserAccountRepository;
import com.coding.repository.UserAccountTransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

public class WithdrawalService {

    private final ATMDenominationRepository atmDenominationRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountTransactionRepository userAccountTransactionRepository;

    public WithdrawalService(ATMDenominationRepository atmDenominationRepository,
                             UserAccountRepository userAccountRepository,
                             UserAccountTransactionRepository userAccountTransactionRepository) {
        this.atmDenominationRepository = atmDenominationRepository;
        this.userAccountRepository = userAccountRepository;
        this.userAccountTransactionRepository = userAccountTransactionRepository;
    }

    public Boolean checkIfMultiples(Integer amount) {
        return amount%100 == 0;
    }

    public Boolean checkATMTransactionLimit(AccountCard accountCard, Integer amount) {
        if (ATMConfig.ATM_BANK_CODE.equals(accountCard.getUserAccount().getBankCode())) {
            return amount <= 25000;
        } else {
            return amount <= 5000;
        }
    }

    public Boolean checkAccountBalance(AccountCard accountCard, Integer amount) {
        Boolean enoughBalance = Boolean.TRUE;
        Double accountBalance = accountCard.getUserAccount().getAmount();
        if (accountBalance < amount) {
            enoughBalance = Boolean.FALSE;
        }
        return enoughBalance;
    }

    public DenominationDTO checkATMBalance(Integer amount) {
        DenominationDTO denominationDTO = new DenominationDTO();
        List<ATMDenomination> atmDenominations = atmDenominationRepository.getAll();
        Integer remainingAmount = amount;
        for (ATMDenomination atmDenomination : atmDenominations) {
            if (remainingAmount != 0) {
                if (atmDenomination.getDenominationValue().equals(2000)
                        && atmDenomination.getGetDenominationCount() >= (remainingAmount/2000)) {
                    denominationDTO.setDenomination_2000_count(remainingAmount/2000);
                    remainingAmount -= (remainingAmount/2000) * 2000;
                } else if (atmDenomination.getDenominationValue().equals(500)
                        && atmDenomination.getGetDenominationCount() >= (remainingAmount/500)) {
                    denominationDTO.setDenomination_500_count(remainingAmount/500);
                    remainingAmount -= (remainingAmount/500) * 500;
                } else if (atmDenomination.getDenominationValue().equals(200)
                        && atmDenomination.getGetDenominationCount() >= (remainingAmount/200)) {
                    denominationDTO.setDenomination_200_count(remainingAmount/200);
                    remainingAmount -= (remainingAmount/200) * 200;
                } else if (atmDenomination.getDenominationValue().equals(100)
                        && atmDenomination.getGetDenominationCount() >= (remainingAmount/100)) {
                    denominationDTO.setDenomination_100_count(remainingAmount/100);
                    remainingAmount -= (remainingAmount/100) * 100;
                }
            }
        }
        return denominationDTO;
    }

    public void withdrawAmount(AccountCard accountCard, DenominationDTO denominationDTO, Integer amount) {
        atmDenominationRepository.updateATMDominations(denominationDTO);
        UserAccount userAccount = userAccountRepository.getById(accountCard.getUserAccount().getId());

        UserAccountTransaction userAccountTransaction = new UserAccountTransaction();
        userAccountTransaction.setUserAccount(userAccount);
        userAccountTransaction.setTransactionType("DEBIT");
        userAccountTransaction.setAmount(amount);
        userAccountTransaction.setTransactionTime(LocalDateTime.now());
        userAccountTransactionRepository.save(userAccountTransaction);

        Double accountBalance = userAccount.getAmount();
        userAccount.setAmount(accountBalance - amount);
        userAccountRepository.save(userAccount);
    }
}
