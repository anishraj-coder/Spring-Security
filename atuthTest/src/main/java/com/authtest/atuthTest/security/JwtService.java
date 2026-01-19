package com.authtest.atuthTest.security;

import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.types.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Getter
public class JwtService {

    private final SecretKey key;
    private final long accessTtl;
    private final long refreshTtl;
    private final String issuer;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-ttl-seconds}") long accessTtl,
            @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtl,
            @Value("${security.jwt.issuer}") String issuer) {

        if(secret==null||secret.length()<64){
            log.error("The secret key for JWT is invalid: {}",secret);
            throw new IllegalArgumentException("The secret key for JWT is invalid: "+secret);
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = accessTtl;
        this.refreshTtl = refreshTtl;
        this.issuer = issuer;
    }

    public String generateAccessToken(AppUser user){
        List<String> roles=user.getRoles()==null?List.of():
                user.getRoles().stream().map(role->"ROLE_"+role.getName()).toList();
        Instant now= Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .issuer(this.issuer)
                .signWith(key)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(this.accessTtl)))
                .claims(Map.of("typ","access","id",user.getId().toString(),"roles",roles))
                .compact();
    }
    public String generateRefreshToken(AppUser user,String jti){
        List<String> roles=user.getRoles()==null?List.of("ROLE_USER"):
                user.getRoles().stream().map(role->"ROLE_"+role.getName()).toList();
        Instant now=Instant.now();
        return Jwts.builder()
                .id(jti)
                .subject(user.getEmail())
                .issuer(this.issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(this.refreshTtl)))
                .claims(Map.of("typ","refresh","roles",roles,"id",user.getId().toString()))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseClaims(String token){
        try{
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        }catch (JwtException e){
            log.error("The token given for claim parsing is invalid: {}",token);
            throw e;
        }
    }

    public boolean isAccessToken(String token){
        Claims c=parseClaims(token).getPayload();
        return "access".equals(c.get("typ"));
    }

    public boolean isRefreshToken(String token){
        Claims c=parseClaims(token).getPayload();
        return "refresh".equals(c.get("typ"));
    }

    public UUID getUserId(String token){
        return UUID.fromString((String)parseClaims(token).getPayload().get("id"));
    }

    public String getJti(String token){
        return parseClaims(token).getPayload().getId();
    }
}
