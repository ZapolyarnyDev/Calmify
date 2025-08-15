package io.github.zapolyarnydev.userservice.repository;

import io.github.zapolyarnydev.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
