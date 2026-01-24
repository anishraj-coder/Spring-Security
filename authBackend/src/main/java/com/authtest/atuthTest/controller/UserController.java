package com.authtest.atuthTest.controller;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.response.UserResponseDto;
import com.authtest.atuthTest.service.UserService;
import com.authtest.atuthTest.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        log.info("incoming post request to controller {}",userDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUserDto(userDto));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@PageableDefault(size = 5)
                                                                     Pageable pageable){
        log.info("incoming post get all to controller ");
        Page<UserDto> users= userService.getAllUsers(pageable);
        var response=users.map(UserHelper::convertUserDtoToResponse);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/single")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam(name = "email")String email){
        var response=UserHelper.convertUserDtoToResponse(userService.getUserByEmail(email));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponseDto> getUserInfo(Authentication authentication){
        String email=authentication.getName();
        var response=UserHelper.convertUserDtoToResponse(userService.getUserByEmail(email));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@RequestParam(name = "user_id")String userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestParam(name="user_id")String  userId,
                                                      UserDto userDto,Authentication authentication){

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(UserHelper.convertUserDtoToResponse(userService.updateUser(userDto,userId
                        ,authentication)));
    }

    @PatchMapping("/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> verifyUser(@RequestParam(name="user_id")String userId){
        UserDto user=userService.verifyUser(userId);
        var response=UserHelper.convertUserDtoToResponse(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PatchMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> changeRole(
            @RequestParam String user_id,
            @RequestParam String role_name) {
        return ResponseEntity.ok(UserHelper.convertUserDtoToResponse(userService
                .changeUserRole(user_id, role_name)));
    }

}
