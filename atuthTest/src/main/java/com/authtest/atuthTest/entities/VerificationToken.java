package com.authtest.atuthTest.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(nullable = false,name = "user_id")
    private AppUser user;

    private Instant expiresAt;

    public boolean isExpired(){
        return Instant.now().isAfter(this.expiresAt);
    }
}
