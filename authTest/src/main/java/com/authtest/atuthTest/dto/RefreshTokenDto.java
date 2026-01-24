package com.authtest.atuthTest.dto;

import com.authtest.atuthTest.entities.AppUser;

import java.time.Instant;

public record RefreshTokenDto(String token, Instant issuedAt, Instant expiresAt, AppUser user) {
    public static RefreshTokenDto of(String token, Instant issuedAt, Instant expiresAt,AppUser user){
        return new RefreshTokenDto(token,issuedAt,expiresAt,user);
    }
}
