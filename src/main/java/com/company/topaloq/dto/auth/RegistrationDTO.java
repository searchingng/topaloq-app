package com.company.topaloq.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Name can't be Empty!")
    private String name;

    @NotBlank(message = "Surame can't be Empty!")
    private String surname;

    @NotBlank(message = "Login can't be Empty!")
    @Size(min = 3, max = 20, message = "Length should be 3-20 characters")
    private String login;

    @NotNull(message = "password is Required")
    @Size(min = 3, max = 20, message = "Length should be 3-20 characters")
    private String password;

    @NotNull(message = "Phone is required")
    private String phone;
}
