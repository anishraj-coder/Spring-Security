package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.service.AuthService;
import com.authtest.atuthTest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    @Override
    public UserDto registerUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
