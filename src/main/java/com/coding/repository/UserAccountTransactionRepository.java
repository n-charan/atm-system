package com.coding.repository;

import com.coding.config.ATMConfig;
import com.coding.model.UserAccountTransaction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserAccountTransactionRepository {

    private List<UserAccountTransaction> userAccountTransactions = ATMConfig.getTransactionList();

    public List<UserAccountTransaction> getTransactionsByAccountId(Long userAccountId) {
        return userAccountTransactions.stream().filter(userAccountTransaction ->
                userAccountTransaction.getUserAccount().getId().equals(userAccountId))
                .sorted(Comparator.comparing(UserAccountTransaction::getId))
                .limit(10)
                .collect(Collectors.toList());

    }

    public void save(UserAccountTransaction userAccountTransaction) {
        long lastId = userAccountTransactions.size();
        userAccountTransaction.setId(lastId + 1);
        userAccountTransactions.add(userAccountTransaction);
    }
}
