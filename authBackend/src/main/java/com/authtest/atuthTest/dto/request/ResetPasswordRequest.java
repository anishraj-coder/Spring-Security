package com.authtest.atuthTest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 5, max = 15,message = "The password must be of length between 5 to 15 characters")
    private String password;
}
