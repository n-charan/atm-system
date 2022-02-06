package main.java.com.coding.repository;

import com.coding.config.ATMConfig;
import com.coding.dto.DenominationDTO;
import com.coding.model.ATMDenomination;

import java.util.List;

public class ATMDenominationRepository {

    private static List<ATMDenomination> atmDenominations = ATMConfig.getAtmDenominations();

    public List<ATMDenomination> getAll() {
        return atmDenominations;
    }

    public void updateATMDominations(DenominationDTO denominationDTO) {
        for (int i = 0; i < atmDenominations.size(); i++) {
            if (atmDenominations.get(i).getDenominationValue() == 2000) {
                ATMDenomination atmDenomination = atmDenominations.get(i);
                atmDenomination.setGetDenominationCount(atmDenomination.getGetDenominationCount() - denominationDTO.getDenomination_2000_count());
                atmDenominations.set(i, atmDenomination);
            } else if (atmDenominations.get(i).getDenominationValue() == 500) {
                ATMDenomination atmDenomination = atmDenominations.get(i);
                atmDenomination.setGetDenominationCount(atmDenomination.getGetDenominationCount() - denominationDTO.getDenomination_500_count());
                atmDenominations.set(i, atmDenomination);
            } else if (atmDenominations.get(i).getDenominationValue() == 200) {
                ATMDenomination atmDenomination = atmDenominations.get(i);
                atmDenomination.setGetDenominationCount(atmDenomination.getGetDenominationCount() - denominationDTO.getDenomination_200_count());
                atmDenominations.set(i, atmDenomination);
            } else if (atmDenominations.get(i).getDenominationValue() == 100) {
                ATMDenomination atmDenomination = atmDenominations.get(i);
                atmDenomination.setGetDenominationCount(atmDenomination.getGetDenominationCount() - denominationDTO.getDenomination_100_count());
                atmDenominations.set(i, atmDenomination);
            }
        }
    }
}
