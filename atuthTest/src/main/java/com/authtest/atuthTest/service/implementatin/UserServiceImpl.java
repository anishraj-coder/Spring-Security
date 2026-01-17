package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.exception.ResourceNotFound;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.service.UserService;
import com.authtest.atuthTest.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail()==null||userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        AppUser user=modelMapper.map(userDto,AppUser.class);
        if(user.getProvider()==null)user.setProvider(Provider.LOCAL);
        AppUser savedUser=userRepository.save(user);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        AppUser user=userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFound("No User found with email: "+email));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId= UserHelper.parseUUID(userId);
        AppUser user=userRepository.findById(uId)
                .orElseThrow(()-> new ResourceNotFound("User not found with id: "+userId));
        userDto.setId(uId);

        if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email id provided already exists");
            }
            user.setEmail(userDto.getEmail());
        }
        Optional.ofNullable(userDto.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        AppUser savedUser=userRepository.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        UUID uId= UserHelper.parseUUID(userId);
        AppUser user=userRepository.findById(uId)
                .orElseThrow(()-> new ResourceNotFound("User not found with id: "+userId));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(String userId) {
        UUID uId=UserHelper.parseUUID(userId);
        AppUser user=userRepository.findById(uId)
                .orElseThrow(()-> new ResourceNotFound("User not found with id: "+userId));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user->modelMapper.map(user,UserDto.class)).toList();
    }


}
