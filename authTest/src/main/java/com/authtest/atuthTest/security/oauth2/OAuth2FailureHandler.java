package com.authtest.atuthTest.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2Login Failed error: {}", exception.getMessage());
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8082/api/auth/login") // Use your actual login path
                .queryParam("error", exception.getLocalizedMessage())
                .encode()
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }
}
