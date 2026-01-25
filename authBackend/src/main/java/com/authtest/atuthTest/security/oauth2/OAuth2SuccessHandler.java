package com.authtest.atuthTest.security.oauth2;

import com.authtest.atuthTest.dto.AccessTokenDto;
import com.authtest.atuthTest.dto.RefreshTokenDto;
import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.RefreshToken;
import com.authtest.atuthTest.entities.types.Role;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.repository.RefreshTokenRepository;
import com.authtest.atuthTest.repository.RoleRepository;
import com.authtest.atuthTest.security.CookieService;
import com.authtest.atuthTest.security.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RoleRepository roleRepository;

    private final JwtService jwtService;
    private final AppUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        log.info("Success Handler entered for the OAuth2");
        OAuth2AuthenticationToken authToken=(OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User=authToken.getPrincipal();
        String registrationId=authToken.getAuthorizedClientRegistrationId();
        OAuth2UserInfo userInfo=OAuth2UserInfoFactory
                .getOAuth2UserInfo(registrationId,oAuth2User.getAttributes());
        if(userInfo.getEmail()==null||userInfo.getEmail().isBlank()){
            throw new BadCredentialsException("The given email is invalid");
        }
        AppUser user=userRepository.findByEmail(userInfo.getEmail())
                .map(existingUser->updateUser(userInfo,existingUser))
                .orElseGet(()->registerUser(userInfo));
        String jti=UUID.randomUUID().toString();
        issueRefreshToken(user,jti,response);
        AccessTokenDto accessTokenDto=jwtService.generateAccessToken(user);
        String targetUrl=determineRedirectUrl(accessTokenDto.token());
        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    private void issueRefreshToken(AppUser user, String jti,
                                   HttpServletResponse response) {
        RefreshTokenDto refreshTokenRecord=jwtService.generateRefreshToken(user,jti);
        RefreshToken rt=RefreshToken.builder()
                .replaceBy(null)
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(refreshTokenRecord.expiresAt())
                .revoked(false)
                .build();
        RefreshToken savedRt=refreshTokenRepository.save(rt);
        cookieService.attachRefreshCookie(response,refreshTokenRecord.token()
                , jwtService.getRefreshTtl());
        cookieService.addNoStoreHeaders(response);
    }

    private AppUser registerUser(OAuth2UserInfo userInfo) {
        String name= (userInfo.getName()==null||userInfo.getName().isBlank())
                ?userInfo.getEmail():userInfo.getName();
        if(userInfo.getName()==null||userInfo.getName().isBlank())name= userInfo.getName();

        Role roles=roleRepository.findByName("USER")
                .orElseGet(()->roleRepository.save(Role.builder().name("USER").build()));


        AppUser newUser=AppUser.builder()
                .name(name)
                .gender(userInfo.getGender())
                .email(userInfo.getEmail())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .roles(Set.of(roles))
                .image(userInfo.getImageUrl())
                .enabled(true)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .provider(userInfo.getProvider())
                .build();
        return userRepository.save(newUser);
    }

    private AppUser updateUser(OAuth2UserInfo userInfo, AppUser existingUser) {
        existingUser.setUpdatedAt(Instant.now());
        existingUser.setName(userInfo.getName());
        existingUser.setImage(userInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

    private String determineRedirectUrl(String accessToken){
        return "http://localhost:3000/oauth2/redirect?token=" + accessToken;
    }

}
