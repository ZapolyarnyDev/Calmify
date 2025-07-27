package io.github.zapolyarnydev.authservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth-users")
@Getter
@Setter
@NoArgsConstructor
public class AuthUser {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private Instant registeredAt;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AuthRole authRole;

    public AuthUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @PrePersist
    public void prePersist(){
        this.registeredAt = Instant.now();
    }
}
