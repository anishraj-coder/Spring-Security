package com.authtest.atuthTest.service;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;


public interface UserService {
    UserDto createUserDto(UserDto userDto);
    AppUser createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto, String userId, Authentication authentication);
    void deleteUser(String userId);
    UserDto getUserById(String userId);
    Iterable<UserDto> getAllUsers();
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto verifyUser(String email);
    UserDto changeUserRole(String userId, String roleName);
}
