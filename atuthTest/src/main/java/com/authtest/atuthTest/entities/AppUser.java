package com.authtest.atuthTest.entities;

import com.authtest.atuthTest.entities.types.Gender;
import com.authtest.atuthTest.entities.types.Provider;
import com.authtest.atuthTest.entities.types.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.Instant;
import java.util.*;

@Entity
@Table(name="users",indexes = {
        @Index(name = "user_email",columnList = "email")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NullMarked
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true )
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String image;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Provider provider=Provider.LOCAL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles_map",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles()==null||this.getRoles().isEmpty()?
                List.of(new SimpleGrantedAuthority("ROLE_USER")):this.getRoles().stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role.getName())).toList();
    }


    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
