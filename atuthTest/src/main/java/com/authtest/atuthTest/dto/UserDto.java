package com.authtest.atuthTest.dto;

import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private String pass;
    private String name;
    private Gender gender;
    private String image;
    private Provider provider=Provider.LOCAL;
    private Set<RoleDto> roles=new HashSet<>();
    private Instant createdAt=Instant.now();
    private Instant updatedAt=Instant.now();
    private Boolean isEnabled=true;
}
