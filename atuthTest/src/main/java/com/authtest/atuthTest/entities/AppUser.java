package com.authtest.atuthTest.entities;

import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.entities.types.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true )
    private String email;
    private String pass;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String image;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Provider provider=Provider.LOCAL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="user_roles"),name="user_roles")
    private Set<Role> roles=new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt=Instant.now();

    @UpdateTimestamp
    private Instant updatedAt=Instant.now();
    @Builder.Default
    private Boolean isEnabled=true;

    @PrePersist
    protected  void creationTimeStamp(){
        this.setCreatedAt(this.getCreatedAt()==null?Instant.now():this.getCreatedAt());
        this.setUpdatedAt(Instant.now());
    }
    @PreUpdate
    protected void updationTimeStamp(){
        this.setUpdatedAt(Instant.now());
    }
}
