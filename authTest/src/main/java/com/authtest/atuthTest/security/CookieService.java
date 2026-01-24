package com.authtest.atuthTest.security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;


@Getter@Setter
@Service
public class CookieService {
    private final String cookieName;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieDomain;
    private final String cookieSameSite;

    public CookieService(@Value("${security.jwt.refresh-token-cookie-name}") String cookieName,
                         @Value("${security.jwt.cookie-secure}")boolean cookieSecure,
                         @Value("${security.jwt.cookie-http-only}")boolean cookieHttpOnly,
                         @Value("${security.jwt.cookie-same-site}")String cookieSameSite,
                         @Value("${security.jwt.cookie-domain}")String cookieDomain){
        this.cookieDomain=cookieDomain;
        this.cookieSameSite=cookieSameSite;
        this.cookieHttpOnly=cookieHttpOnly;
        this.cookieSecure=cookieSecure;
        this.cookieName=cookieName;
    }

    public void attachRefreshCookie(HttpServletResponse response, String value, long maxAge){
        var responseCookieBuilder=ResponseCookie.from(this.cookieName,value)
                .maxAge(maxAge)
                .path("/")
                .httpOnly(cookieHttpOnly)
                .secure(this.cookieSecure)
                .sameSite(this.cookieSameSite);

        if(cookieDomain!=null&&!cookieDomain.isBlank())
            responseCookieBuilder.domain(cookieDomain);

        ResponseCookie responseCookie=responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());
    }

    public void clearRefreshToken(HttpServletResponse response){
        var responseCookieBuilder=ResponseCookie.from(this.cookieName,"")
                .path("/")
                .httpOnly(this.cookieHttpOnly)
                .secure(this.cookieSecure)
                .sameSite(this.cookieSameSite)
                .maxAge(0);
        if(cookieDomain!=null&&!cookieDomain.isBlank())
            responseCookieBuilder.domain(this.cookieDomain);
        ResponseCookie cookie=responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
    }
    public void addNoStoreHeaders(HttpServletResponse response){
        response.setHeader(HttpHeaders.CACHE_CONTROL,"no-store");
        response.setHeader(HttpHeaders.PRAGMA,"no-cache");
    }

    public Optional<String> readRefreshTokenFromRequest(HttpServletRequest request){
        if(request.getCookies()!=null){
            return Arrays.stream(request.getCookies()).filter(c->getCookieName().equals(c.getName()))
                    .map(Cookie::getValue).filter(v->v!=null&&!v.isBlank()).findFirst();
        }
        String refreshHeader=request.getHeader("X-Refresh-Token");
        if(refreshHeader!=null&&!refreshHeader.isBlank()){
            return Optional.of(refreshHeader.trim());
        }
        return Optional.empty();
    }
}
