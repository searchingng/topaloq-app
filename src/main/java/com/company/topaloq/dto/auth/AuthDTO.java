package com.company.topaloq.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AuthDTO {

    @NotBlank(message = "Login can't be Empty!")
    @Size(min = 3, max = 20, message = "Length should be 3-20 characters")
    private String login;

    @NotNull(message = "password is Required")
    @Size(min = 3, max = 20, message = "Length should be 3-20 characters")
    private String password;
}
