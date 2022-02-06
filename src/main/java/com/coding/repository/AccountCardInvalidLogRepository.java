package com.coding.repository;

import com.coding.model.AccountCardInvalidLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountCardInvalidLogRepository {

    private List<AccountCardInvalidLog> accountCardInvalidLogs = new ArrayList<>();

    public List<AccountCardInvalidLog> getAllByAccountCardId(Long accountCardId) {
        return accountCardInvalidLogs.stream().filter(accountCardInvalidLog ->
                    accountCardInvalidLog.getAccountCard().getId().equals(accountCardId))
                .collect(Collectors.toList());
    }

    public AccountCardInvalidLog save(AccountCardInvalidLog accountCardInvalidLog) {
        long lastId = accountCardInvalidLogs.size();
        accountCardInvalidLog.setId(lastId + 1);
        accountCardInvalidLogs.add(accountCardInvalidLog);
        return accountCardInvalidLog;
    }
}
