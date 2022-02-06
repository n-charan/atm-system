package com.coding.repository;

import com.coding.dto.DenominationDTO;
import com.coding.model.ATMDenomination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ATMDenominationRepositoryTest {

    @Test
    @DisplayName("Test to update denomination")
    public void updateATMDenominationsTest() {
        ATMDenominationRepository atmDenominationRepository = new ATMDenominationRepository();
        DenominationDTO denominationDTO = new DenominationDTO();
        denominationDTO.setDenomination_100_count(5);
        denominationDTO.setDenomination_200_count(2);
        denominationDTO.setDenomination_500_count(1);

        atmDenominationRepository.updateATMDominations(denominationDTO);
        List<ATMDenomination> atmDenominations = atmDenominationRepository.getAll();

        for (ATMDenomination denomination : atmDenominations) {
            if (denomination.getDenominationValue().equals(2000)) {
                Assertions.assertEquals(5, denomination.getGetDenominationCount());
            } else if (denomination.getDenominationValue().equals(500)) {
                Assertions.assertEquals(9, denomination.getGetDenominationCount());
            } else if (denomination.getDenominationValue().equals(200)) {
                Assertions.assertEquals(28, denomination.getGetDenominationCount());
            } else if (denomination.getDenominationValue().equals(100)) {
                Assertions.assertEquals(45, denomination.getGetDenominationCount());
            }
        }
    }
}
