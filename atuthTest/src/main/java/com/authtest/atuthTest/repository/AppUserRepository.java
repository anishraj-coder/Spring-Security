package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.dto.UserDto;
import com.authtest.atuthTest.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);
}