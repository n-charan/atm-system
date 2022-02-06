package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountTransaction {

    private Long id;

    private UserAccount userAccount;

    private double amount;

    private String transactionType;

    private LocalDateTime transactionTime;
}
