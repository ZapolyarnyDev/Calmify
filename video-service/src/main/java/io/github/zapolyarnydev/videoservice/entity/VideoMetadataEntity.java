package io.github.zapolyarnydev.videoservice.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.github.zapolyarnydev.commons.constants.GenerationConstants;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@Entity
@Table(name = "video_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false, updatable = false)
    private String shortId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false, updatable = false)
    private Instant uploadedAt;

    @Builder
    public VideoMetadataEntity(Supplier<String> shortIdGenerator, String title, String description, Instant uploadedAt) {
        this.title = title;
        this.description = description;
        this.uploadedAt = uploadedAt;
        this.shortId = shortIdGenerator.get();
    }

    @PrePersist
    private void prePersist() {
        if(shortId == null) {
            char[] alphabet = GenerationConstants.ALPHABET.toCharArray();
            shortId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabet, 10);
        }

        if (uploadedAt == null) {
            uploadedAt = Instant.now();
        }
    }
}
