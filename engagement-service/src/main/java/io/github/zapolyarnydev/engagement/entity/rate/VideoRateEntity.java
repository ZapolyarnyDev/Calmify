package io.github.zapolyarnydev.engagement.entity.rate;

import io.github.zapolyarnydev.engagement.domain.rate.VideoRate;
import io.github.zapolyarnydev.engagement.domain.type.RateType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "video_rates")
@Getter
@Setter
@NoArgsConstructor
public class VideoRateEntity implements VideoRate {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID targetId;

    @Column(nullable = false, updatable = false)
    private UUID authorId;

    @Enumerated(EnumType.STRING)
    private RateType rate;

    @Builder
    public VideoRateEntity(UUID targetId, UUID authorId, RateType rate) {
        this.targetId = targetId;
        this.authorId = authorId;
        this.rate = rate;
    }

    @Override
    public void changeRate(RateType rate) {
        this.rate = rate;
    }
}
