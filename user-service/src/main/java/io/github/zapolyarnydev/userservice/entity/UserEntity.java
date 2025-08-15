package io.github.zapolyarnydev.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "calmify_users")
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String displayName;

    @Column(unique = true, nullable = false)
    private String handle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean showActivity = true;

    @Column(nullable = false)
    private Instant lastSeenAt;

    @Column(nullable = false, updatable = false)
    private Instant registeredAt;

    public UserEntity(String email) {
        this.email = email;
        this.lastSeenAt = Instant.now();
    }

    @PrePersist
    public void prePersist() {
        if(displayName == null) {
            displayName = email.split("@")[0];
        }
        if(handle == null) {
            String base = displayName.toLowerCase().replaceAll("[^a-z0-9]", "");
            String suffix = UUID.randomUUID().toString().substring(0, 8);
            handle = base + suffix;
        }
        if(lastSeenAt == null) {
            lastSeenAt = Instant.now();
        }
        if (registeredAt == null) {
            registeredAt = Instant.now();
        }
    }

}
