package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.AccessTokenDto;
import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.LoginResponseDto;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.RefreshToken;
import com.authtest.atuthTest.dto.RefreshTokenDto;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.repository.RefreshTokenRepository;
import com.authtest.atuthTest.security.CookieService;
import com.authtest.atuthTest.security.JwtService;
import com.authtest.atuthTest.service.AuthService;
import com.authtest.atuthTest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;
    @Transactional
    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        UserDto userDto=UserDto.builder()
                .email(request.getEmail())
                .name(request.getEmail())
                .image(request.getImage())
                .gender(request.getGender())
                .password(Objects.requireNonNull(passwordEncoder.encode(request.getPassword())))
                .build();
        UserDto createdUser=userService.createUser(userDto);

        return modelMapper.map(createdUser,RegisterResponse.class);
    }

    @Override
    public Authentication authenticateUser(String email,String password){
        try{
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email,password));
        }catch (Exception e){
            log.error("Invalid credentials for the request: {}",email);
            throw new BadCredentialsException("Invalid email or password: "+email);
        }
    }

    @Transactional
    @Override
    public LoginResponseDto loginUser(String email, String password, HttpServletResponse response) {
        Authentication authentication=authenticateUser(email,password);
        AppUser user=(AppUser) authentication.getPrincipal();
        String jti=UUID.randomUUID().toString();
        RefreshToken refreshTokenObject=RefreshToken.builder()
                .jti(jti)
                .replaceBy(null)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtl()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenObject);
        String accessToken=jwtService.generateAccessToken(user).token();
        String refreshToken=jwtService.generateRefreshToken(user, jti).token();

        cookieService.attachRefreshCookie(response,refreshToken, jwtService.getRefreshTtl());
        cookieService.addNoStoreHeaders(response);

        return LoginResponseDto.of(email,accessToken,null,
                jwtService.getAccessTtl(),"Bearer");
    }

    @Transactional
    @Override
    public LoginResponseDto refreshUser(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken=cookieService.readRefreshTokenFromRequest(request)
                .orElseThrow(()->new IllegalArgumentException("The refresh token not found"));
        if(!jwtService.isRefreshToken(refreshToken))
            throw new BadCredentialsException("The given token is an invalid refresh token: "
                    +refreshToken);
        String jti=jwtService.getJti(refreshToken);
        UUID userId=jwtService.getUserId(refreshToken);
        RefreshToken savedRefreshToken=refreshTokenRepository.findByJti(jti).orElseThrow(()->{
            log.error("The JTI for refresh token was not found in DB: {}",jti);
            throw new IllegalArgumentException("The given JTI not found in the DB");
        });

        if(savedRefreshToken.isRevoked()){
            log.error("The token was revoked");
            throw new BadCredentialsException("The token was revoked");
        }
        if(savedRefreshToken.getExpiresAt().isBefore(Instant.now())){
            log.error("The token is expired");
            throw new BadCredentialsException("The token was expired");
        }

        if(!savedRefreshToken.getUser().getId().equals(userId)){
            throw new BadCredentialsException("You can't access jwt token of other user");
        }


        savedRefreshToken.setRevoked(true);
        String newJti=UUID.randomUUID().toString();
        savedRefreshToken.setReplaceBy(newJti);

        RefreshTokenDto newRefreshToken=jwtService
                .generateRefreshToken(savedRefreshToken.getUser(),newJti);
        RefreshToken newRefreshTokenObj=RefreshToken.builder()
                .jti(newJti)
                .createdAt(newRefreshToken.issuedAt())
                .user(savedRefreshToken.getUser())
                .expiresAt(newRefreshToken.expiresAt())
                .revoked(false)
                .replaceBy(null)
                .build();
        RefreshToken newSavedRefreshTokenObj=refreshTokenRepository.save(newRefreshTokenObj);
        refreshTokenRepository.save(savedRefreshToken);

        long refreshTokenDuration = newRefreshToken.expiresAt().getEpochSecond()
                - Instant.now().getEpochSecond();
        cookieService.attachRefreshCookie(response,newRefreshToken.token(),refreshTokenDuration);
        AccessTokenDto newAccessToken=jwtService.generateAccessToken(savedRefreshToken.getUser());
        long accessTokenDuration=newAccessToken.expiredAt().getEpochSecond()
                -Instant.now().getEpochSecond();
        return  LoginResponseDto.of(newSavedRefreshTokenObj.getUser().getEmail(), newAccessToken.token(),
                null, accessTokenDuration,"Bearer");
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        cookieService.readRefreshTokenFromRequest(request)
                .ifPresent(token->{
                    try{
                        String jti = jwtService.getJti(token);
                        refreshTokenRepository.findByJti(jti).ifPresent(rt -> {
                            rt.setRevoked(true);
                            refreshTokenRepository.save(rt);
                            log.info("Successfully revoked token JTI: {}", jti);
                        });
                    }catch (Exception e){
                        log.error("The attempt to revoke the refresh token failed for logout");
                    }
                });
        cookieService.addNoStoreHeaders(response);
        cookieService.clearRefreshToken(response);
        SecurityContextHolder.clearContext();
    }
}
