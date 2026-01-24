package com.authtest.atuthTest.utils;

import com.authtest.atuthTest.dto.RoleDto;
import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.response.UserResponseDto;
import com.authtest.atuthTest.entities.types.Role;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserHelper {
    public static UUID parseUUID(String uuid){
        return UUID.fromString(uuid);
    }

    public static UserResponseDto convertUserDtoToResponse(UserDto userDto){
        return UserResponseDto.builder()
                .id(userDto.getId().toString())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .image(userDto.getImage())
                .roles(userDto.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet()))
                .gender(userDto.getGender())
                .enabled(userDto.getEnabled())
                .build();
    }
}
