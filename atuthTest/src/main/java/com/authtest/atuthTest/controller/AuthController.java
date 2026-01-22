package com.authtest.atuthTest.controller;

import com.authtest.atuthTest.dto.request.ForgetPasswordRequest;
import com.authtest.atuthTest.dto.request.LoginRequest;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.request.ResetPasswordRequest;
import com.authtest.atuthTest.dto.response.LoginResponseDto;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import com.authtest.atuthTest.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Validated @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Validated @RequestBody LoginRequest request,
                                                      HttpServletResponse response){
        return ResponseEntity.ok(authService.loginUser(request.getEmail(),request.getPassword(),response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletResponse response,
                                                         HttpServletRequest request){
        return ResponseEntity.ok(authService.refreshUser(request,response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response,HttpServletRequest request){
        authService.logout(request,response);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/login")
    public ResponseEntity<String> loginError(@RequestParam(required = false) String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Login Failed: " + error);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@Validated @RequestBody ForgetPasswordRequest request){
        return ResponseEntity.ok(authService.generatePasswordResetToken(request.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Validated @RequestBody ResetPasswordRequest request){
        authService.verifyAndResetPassword(request.getToken(),request.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token){
        authService.verifyUser(token);
        return ResponseEntity.ok("The user is now verified");
    }
}
