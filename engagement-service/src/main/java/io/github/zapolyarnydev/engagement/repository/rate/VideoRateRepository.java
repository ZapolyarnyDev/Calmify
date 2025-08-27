package io.github.zapolyarnydev.engagement.repository.rate;

import io.github.zapolyarnydev.engagement.entity.rate.VideoRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRateRepository extends JpaRepository<VideoRateEntity, Long> {
    Optional<VideoRateEntity> findByTargetIdAndAuthorId(UUID targetId, UUID authorId);
}
