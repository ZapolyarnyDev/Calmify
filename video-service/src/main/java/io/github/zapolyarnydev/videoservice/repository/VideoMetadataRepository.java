package io.github.zapolyarnydev.videoservice.repository;

import io.github.zapolyarnydev.videoservice.entity.VideoMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadataEntity, UUID> {

    boolean existsByShortId(String shortId);

    Optional<VideoMetadataEntity> findByShortId(String shortId);
}
