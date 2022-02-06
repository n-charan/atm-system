package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCardInvalidLog {

    private Long id;

    private AccountCard accountCard;

    private LocalDateTime wrongPinTime;
}
