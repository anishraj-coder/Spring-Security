package com.authtest.atuthTest.dto.request;

import com.authtest.atuthTest.entities.types.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    @Length(max = 50,min = 2,message = "The name should be between 2 and 50")
    private String name;
    @Email
    private String email;
    @NotNull
    @Length(max = 15,min=5, message = "password must be 5 to 15 character long")
    private String password;
    @NotNull
    private Gender gender;
    private String image;
}
