package com.authtest.atuthTest.service;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.entities.AppUser;

public interface UserService {
    UserDto createUserDto(UserDto userDto);
    AppUser createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto,String userId);
    void deleteUser(String userId);
    UserDto getUserById(String userId);
    Iterable<UserDto> getAllUsers();
}
