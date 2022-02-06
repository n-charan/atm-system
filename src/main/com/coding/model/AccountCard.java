package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCard {

    private Long id;

    private UserAccount userAccount;

    private String cardNumber;

    private String expiryMonth;

    private String expiryYear;

    private String pin;

    private Boolean isBlocked;
}
