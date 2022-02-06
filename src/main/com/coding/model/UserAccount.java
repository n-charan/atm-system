package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAccount {

    private Long id;

    private User user;

    private String accountNumber;

    private String accountType;

    private double amount;

    private String bankCode;
}
