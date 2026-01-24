package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.entities.types.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String user);
}