package com.authtest.atuthTest.dto;

import com.authtest.atuthTest.entities.AppUser;

import java.time.Instant;

public record AccessTokenDto(String token, Instant issuedAt, Instant expiredAt, AppUser user) {
    public static AccessTokenDto of(String token,Instant issuedAt,Instant expiredAt,AppUser user){
        return new AccessTokenDto(token,issuedAt,expiredAt,user);
    }
}
