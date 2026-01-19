package com.authtest.atuthTest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Primary
@Component
@Slf4j

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver exceptionResolver;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    // JwtAuthenticationFilter.java
    public JwtAuthenticationFilter(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,
            UserDetailsService userDetailsService,
            JwtService jwtService) {
        this.exceptionResolver = exceptionResolver;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming request from URI: {}",request.getRequestURI());
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header=request.getHeader("Authorization");
        if(header==null||!header.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String token=header.substring(7);
        log.info("Incoming request for {} in filter chain, Jwt: {}",request.getPathInfo(),token);
        try{
            Claims claims=jwtService.parseClaims(token).getPayload();
            String email=claims.getSubject();
            if(email!=null&&"access".equals(claims.get("typ"))&&
                    SecurityContextHolder.getContext().getAuthentication()==null){
                List<String> roles = (List<String>) claims.get("roles");
                List<SimpleGrantedAuthority>authorities=roles.stream()
                        .map(SimpleGrantedAuthority::new).toList();
                UsernamePasswordAuthenticationToken userToken=new UsernamePasswordAuthenticationToken(
                        email,null ,authorities
                );
                userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userToken);
            }
        }catch (ExpiredJwtException e){
            log.error("JWT token is expired: {}",token);
            exceptionResolver.resolveException(request,response,null,e);
            return;
        }catch (MalformedJwtException e){
            log.error("JWT token is malformed: {}",token);
            exceptionResolver.resolveException(request,response,null,e);
            return;
        }catch(JwtException e){
            log.error("JWT exception occurred, token: {}",token);
            exceptionResolver.resolveException(request,response,null,e);
            return;
        }
        filterChain.doFilter(request,response);
    }


}
