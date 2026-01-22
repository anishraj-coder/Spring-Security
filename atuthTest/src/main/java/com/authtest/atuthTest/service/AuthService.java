package com.authtest.atuthTest.service;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.LoginResponseDto;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public RegisterResponse registerUser(RegisterRequest userDto);
    public Authentication authenticateUser(String email,String password);
    public LoginResponseDto loginUser(String email, String password, HttpServletResponse response);
    public LoginResponseDto refreshUser(HttpServletRequest request, HttpServletResponse response);
    public void logout(HttpServletRequest request,HttpServletResponse response);
    public String generatePasswordResetToken(String email);
    public void verifyAndResetPassword(String token,String newPassword);
    public void verifyUser(String token);
}
