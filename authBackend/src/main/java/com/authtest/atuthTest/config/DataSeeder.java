package com.authtest.atuthTest.config;

import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.entities.types.Role;
import com.authtest.atuthTest.repository.AppUserRepository;
import com.authtest.atuthTest.repository.RoleRepository;
import com.authtest.atuthTest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component

public class DataSeeder implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;

    private final String adminPass;
    private final String userPass;

    public DataSeeder(UserService userService, RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder, AppUserRepository userRepository,
                      @Value("${security.credentials.admin}")String adminPass,
                      @Value("${security.credentials.user}")String userPass){
        this.userService=userService;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
        this.adminPass=adminPass;
        this.userPass=userPass;
    }

    @Override
    public void run(String... args) {

        AppUser user=userRepository.findByEmail("admin1@test.com").orElse(null);

        if(user!=null)return;

        // 1. Ensure Roles exist
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

        // 2. Add 2 ADMIN Users (Enabled)
        for (int i = 1; i <= 2; i++) {
            createUser("admin" + i + "@test.com", adminPass, "Admin User " + i, Set.of(adminRole), true);
        }

        // 3. Add 10 USER Users (Mixed enabled/disabled)
        for (int i = 1; i <= 10; i++) {
            boolean isEnabled = i % 2 == 0; // Even numbers enabled, odd numbers disabled
            createUser("user" + i + "@test.com", userPass, "Regular User " + i, Set.of(userRole), isEnabled);
        }
    }

    private void createUser(String email, String pass, String name, Set<Role> roles, boolean enabled) {
        if (userRepository.existsByEmail(email)) return;

        AppUser user = AppUser.builder()
                .email(email)
                .password(passwordEncoder.encode(pass))
                .name(name)
                .roles(roles)
                .gender(Gender.MALE)
                .enabled(enabled)
                .provider(Provider.LOCAL)
                .build();

        userRepository.save(user);
    }
}