package io.github.zapolyarnydev.engagement.entity;

import io.github.zapolyarnydev.engagement.domain.Engagement;
import io.github.zapolyarnydev.engagement.domain.Target;
import io.github.zapolyarnydev.engagement.domain.type.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "engagement_video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EngagementVideoEntity implements Engagement<UUID, UUID>, Target {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID targetId;

    @Override
    public TargetType getTargetType() {
        return TargetType.VIDEO;
    }
}
