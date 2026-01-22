package com.authtest.atuthTest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = "invalid email")
    @NotNull(message = "email cant be null")
    private  String email;
    @NotNull(message = "password cant be null")
    @Length(max=15, min=5 ,message = "max length is 15 and min is 5")
    private  String password;
}
