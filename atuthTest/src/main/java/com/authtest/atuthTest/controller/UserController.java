package com.authtest.atuthTest.controller;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        log.info("incoming post request to controller {}",userDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        log.info("incoming post get all to controller ");
        return ResponseEntity.ok(userService.getAllUsers());
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
    public ResponseEntity<UserDto> updateUser(@RequestParam(name="user_id")String  userId,UserDto userDto){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(userDto,userId));
    }
}
