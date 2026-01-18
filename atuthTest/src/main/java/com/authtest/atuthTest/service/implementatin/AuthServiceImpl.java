package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.request.RegisterRequest;
import com.authtest.atuthTest.dto.response.RegisterResponse;
import com.authtest.atuthTest.service.AuthService;
import com.authtest.atuthTest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
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
}
