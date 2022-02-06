package com.coding.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DenominationDTO {

    private Integer denomination_2000_count = 0;

    private Integer denomination_500_count = 0;

    private Integer denomination_200_count = 0;

    private Integer denomination_100_count = 0;

}
