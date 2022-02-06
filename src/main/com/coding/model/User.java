package com.coding.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;
}
