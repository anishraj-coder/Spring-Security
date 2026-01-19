package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.LoginResponseDto;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.types.Role;
import com.authtest.atuthTest.exception.ResourceNotFound;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.security.JwtService;
import com.authtest.atuthTest.service.AuthService;
import com.authtest.atuthTest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
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

    @Override
    public LoginResponseDto loginUser(String email, String password) {
        Authentication authentication=authenticateUser(email,password);
        AppUser user=(AppUser) authentication.getPrincipal();

        String accessToken=jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user, UUID.randomUUID().toString());
        return LoginResponseDto.of(email,accessToken,refreshToken,
                jwtService.getAccessTtl(),"Bearer");
    }
}
