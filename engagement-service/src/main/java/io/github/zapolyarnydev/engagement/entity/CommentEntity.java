package io.github.zapolyarnydev.engagement.entity;


import io.github.zapolyarnydev.engagement.domain.Comment;
import io.github.zapolyarnydev.engagement.domain.type.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class CommentEntity implements Comment<Long, UUID, UUID> {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID targetId;

    @Column(nullable = false, updatable = false)
    private UUID authorId;

    @Column(nullable = false)
    private String text;

    private Instant publishedAt;

    private Instant editedAt;

    @Builder
    public CommentEntity(Long id, UUID targetId, UUID authorId, String text, Instant publishedAt) {
        this.id = id;
        this.targetId = targetId;
        this.authorId = authorId;
        this.text = text;
        this.publishedAt = publishedAt;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.COMMENT;
    }

    public void editText(String text) {
        this.text = text;
        this.editedAt = Instant.now();
    }

    @PrePersist
    private void prePersist() {
        if(publishedAt == null)
            publishedAt = Instant.now();

        if(editedAt == null)
            editedAt = publishedAt;
    }
}
