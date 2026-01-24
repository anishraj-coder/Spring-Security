package com.authtest.atuthTest.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token",indexes = {
        @Index(name = "reset_token_user_id_idx",columnList = "user_id"),
        @Index(name = "reset_token_token_idx",columnList = "token")
})
@Getter@Setter@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)
    private String token;

    @OneToOne(targetEntity = AppUser.class,optional = false,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private AppUser user;

    private Instant expiresAt;

    public boolean isExpired(){
        return Instant.now().isAfter(expiresAt);
    }
}
