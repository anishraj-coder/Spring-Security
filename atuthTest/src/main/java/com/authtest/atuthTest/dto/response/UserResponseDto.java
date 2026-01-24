package com.authtest.atuthTest.dto.response;

import com.authtest.atuthTest.entities.types.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private String id;
    private String name;
    private String image;
    private Set<String> roles;
    private String email;
    private Gender gender;
    private boolean enabled;
}
