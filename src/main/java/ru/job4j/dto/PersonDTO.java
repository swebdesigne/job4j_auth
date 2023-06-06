package ru.job4j.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {
    @NotBlank(message = "the password must be not empty")
    @Size(min = 5, message = "the password should not be less than 5")
    private String password;
}
