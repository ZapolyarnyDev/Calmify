package io.github.zapolyarnydev.engagement.repository;

import io.github.zapolyarnydev.engagement.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
