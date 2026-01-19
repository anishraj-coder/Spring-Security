package com.authtest.atuthTest.service;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.LoginResponseDto;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public RegisterResponse registerUser(RegisterRequest userDto);
    public Authentication authenticateUser(String email,String password);
    public LoginResponseDto loginUser(String email, String password);
}
