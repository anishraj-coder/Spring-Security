package com.authtest.atuthTest.dto;

import jakarta.persistence.GeneratedValue;
import lombok.*;

import java.util.UUID;


@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private UUID id;
    private String name;
}
