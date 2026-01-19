package com.authtest.atuthTest.dto.response;


import com.authtest.atuthTest.dto.request.LoginRequestDto;

public record LoginResponseDto(String email,
                               String jwt, String freshToken, long expiresIn, String tokenType) {
    public static LoginResponseDto of(String email, String jwt, String freshToken,
                                      long expiresIn,String tokenType) {
        return new LoginResponseDto(email, jwt, freshToken, expiresIn, tokenType);
    }
}
