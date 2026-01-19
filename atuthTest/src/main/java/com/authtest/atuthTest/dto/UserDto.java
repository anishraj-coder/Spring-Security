package com.authtest.atuthTest.dto;

import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;
import lombok.*;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NullMarked
public  class UserDto {
    private UUID id;
    private String email;
    private String password;
    private String name;
    private Gender gender;
    private String image;
    private Provider provider=Provider.LOCAL;
    private Set<String> roles=new HashSet<>();
    private Instant createdAt=Instant.now();
    private Instant updatedAt=Instant.now();
    private Boolean isEnabled=true;
}
