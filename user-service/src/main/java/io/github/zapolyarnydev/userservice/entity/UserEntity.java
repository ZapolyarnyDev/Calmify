package io.github.zapolyarnydev.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "calmify_users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Setter
    private String displayName;

    @Column(unique = true, nullable = false)
    @Setter
    private String handle;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String description;

    @Column(nullable = false)
    @Setter
    private boolean showActivity = true;

    @Column(nullable = false)
    @Setter
    private Instant lastSeenAt;

    @Column(nullable = false, updatable = false)
    private Instant registeredAt;

    public UserEntity(String email, Instant registeredAt) {
        this.email = email;
        this.lastSeenAt = registeredAt;
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
