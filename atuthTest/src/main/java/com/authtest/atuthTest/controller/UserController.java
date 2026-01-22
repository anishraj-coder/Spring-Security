package com.authtest.atuthTest.controller;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.dto.response.UserResponseDto;
import com.authtest.atuthTest.service.UserService;
import com.authtest.atuthTest.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Iterable<UserResponseDto>> getAllUsers(){
        log.info("incoming post get all to controller ");
        Iterable<UserDto> users= userService.getAllUsers();
        Set<UserResponseDto> response= StreamSupport.stream(users.spliterator(),false)
                .map(UserHelper::convertUserDtoToResponse)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/single")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam(name = "email")String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam(name = "user_id")String userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestParam(name="user_id")String  userId, UserDto userDto){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(UserHelper.convertUserDtoToResponse(userService.updateUser(userDto,userId)));
    }
}
