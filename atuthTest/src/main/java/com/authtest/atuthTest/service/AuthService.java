package com.authtest.atuthTest.service;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.RegisterResponse;

public interface AuthService {
    public RegisterResponse registerUser(RegisterRequest userDto);
}
