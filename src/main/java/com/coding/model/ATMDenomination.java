package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ATMDenomination {

    private Long id;

    private Integer denominationValue;

    private Integer getDenominationCount;
}
