package com.authtest.atuthTest.service.implementatin;

import com.authtest.atuthTest.dto.RoleDto;
import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.entities.types.Role;
import com.authtest.atuthTest.exception.ResourceNotFound;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.repository.RefreshTokenRepository;
import com.authtest.atuthTest.repository.RoleRepository;
import com.authtest.atuthTest.repository.VerificationTokenRepository;
import com.authtest.atuthTest.service.UserService;
import com.authtest.atuthTest.utils.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final AppUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDto createUserDto(UserDto userDto) {

        AppUser savedUser=createUser(userDto);
        UserDto response=modelMapper.map(savedUser,UserDto.class);
        response.setRoles(savedUser.getRoles().stream()
                .map((element) -> modelMapper.map(element, RoleDto.class))
                .collect(Collectors.toSet()));
        return response;
    }

    @Override
    public AppUser createUser(UserDto userDto) {
        if(userDto.getEmail()==null||userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        AppUser user=modelMapper.map(userDto,AppUser.class);
        if(user.getProvider()==null)user.setProvider(Provider.LOCAL);
        if(userDto.getRoles()==null||userDto.getRoles().isEmpty()){
            Role role = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
            user.setRoles(Set.of(role));
        }else{
            Set<Role> rolesToAssign=new HashSet<>();
            for(RoleDto role:userDto.getRoles()){
                Role existingRole=roleRepository.findByName(role.getName().toUpperCase())
                        .orElseGet(()->roleRepository
                                .save(Role.builder().name(role.getName().toUpperCase()).build()));
                rolesToAssign.add(existingRole);
            }
            user.setRoles(rolesToAssign);
        }
        return userRepository.save(user);
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
    public UserDto updateUser(UserDto userDto, String userId, Authentication authentication) {

        UUID targetUserId = UserHelper.parseUUID(userId);

        AppUser targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new ResourceNotFound("User not found with id: " + userId));

        AppUser requestingUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("Requester is invalid, email: {}", authentication.getName());
                    return new BadCredentialsException("The requester is invalid");
                });

        boolean isAdmin = requestingUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));

        boolean isSelf = requestingUser.getId().equals(targetUserId);

        if (!isAdmin && !isSelf) {
            throw new IllegalArgumentException("You are not allowed to update this user");
        }

        userDto.setId(targetUserId);

        if (userDto.getEmail() != null && !targetUser.getEmail().equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email id provided already exists");
            }
            targetUser.setEmail(userDto.getEmail());
        }

        Optional.ofNullable(userDto.getGender()).ifPresent(targetUser::setGender);
        Optional.ofNullable(userDto.getName()).ifPresent(targetUser::setName);

        AppUser savedUser = userRepository.save(targetUser);
        return modelMapper.map(savedUser, UserDto.class);
    }


    @Override
    @Transactional
    public void deleteUser(String userId) {
        UUID uId= UserHelper.parseUUID(userId);
        AppUser user=userRepository.findById(uId)
                .orElseThrow(()-> new ResourceNotFound("User not found with id: "+userId));
        String email=user.getEmail();
        refreshTokenRepository.findByUser(user).forEach(refreshTokenRepository::delete);
        verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);
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

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public UserDto verifyUser(String userID) {
        UUID uuid=UUID.fromString(userID);
        AppUser user=userRepository.findById(uuid)
                .orElseThrow(()->{
                    log.error("The user with user {} does not exist",userID);
                    return new BadCredentialsException("The user does not exist");
                });
        user.setEnabled(true);
        AppUser savedUser=userRepository.save(user);
        Set<RoleDto> roles=savedUser.getRoles().stream().map(r->modelMapper.map(r,RoleDto.class))
                .collect(Collectors.toSet());
        UserDto savedDto=modelMapper.map(savedUser,UserDto.class);
        savedDto.setRoles(roles);
        return savedDto;
    }

    @Override
    @Transactional
    public UserDto changeUserRole(String userId, String roleName) {
        UUID uId = UserHelper.parseUUID(userId);
        AppUser user = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFound("Role not found: " + roleName));

        user.setRoles(Set.of(role));
        AppUser savedUser = userRepository.save(user);

        // Invalidate sessions so the user must re-log in to get the new role in their JWT
        refreshTokenRepository.findByUser(savedUser).forEach(refreshTokenRepository::delete);

        return modelMapper.map(savedUser, UserDto.class);
    }


}
