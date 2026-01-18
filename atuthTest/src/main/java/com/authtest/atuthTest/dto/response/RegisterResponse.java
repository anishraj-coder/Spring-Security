package com.authtest.atuthTest.dto.response;

import com.authtest.atuthTest.entities.types.Gender;
import lombok.*;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@Setter@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public @NullMarked class RegisterResponse {
    private UUID id;
    private String name;
    private Gender gender;
    private String image;
}
