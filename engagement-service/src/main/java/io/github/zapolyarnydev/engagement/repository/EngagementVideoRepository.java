package io.github.zapolyarnydev.engagement.repository;

import io.github.zapolyarnydev.engagement.entity.EngagementVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EngagementVideoRepository extends JpaRepository<EngagementVideoEntity, UUID> {

    Optional<EngagementVideoEntity> findByTargetId(UUID targetId);

}
