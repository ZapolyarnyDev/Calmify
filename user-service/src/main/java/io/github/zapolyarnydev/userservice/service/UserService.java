package io.github.zapolyarnydev.userservice.service;

import io.github.zapolyarnydev.commons.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.commons.exception.UserNotFoundException;
import io.github.zapolyarnydev.userservice.dto.UpdateUserRequestDTO;
import io.github.zapolyarnydev.userservice.entity.UserEntity;
import io.github.zapolyarnydev.userservice.exception.HandleAlreadyTakenException;
import io.github.zapolyarnydev.userservice.exception.IllegalHandleSizeException;
import io.github.zapolyarnydev.userservice.exception.InvalidHandleCharactersException;
import io.github.zapolyarnydev.userservice.mapper.UpdateRequestMapper;
import io.github.zapolyarnydev.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UpdateRequestMapper updateRequestMapper;

    @Transactional
    public UserEntity createUser(String email, Instant registeredAt) throws EmailAlreadyUsedException {
        if(userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        String handle = generateHandle(email.split("@")[0]);

        var entity = new UserEntity(email, registeredAt);
        entity.setHandle(handle);

        log.info("User created! Email: {}, Handle: {}", email, handle);
        return userRepository.save(entity);
    }

    public UserEntity findUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Transactional
    public void updateUser(UserEntity userEntity, UpdateUserRequestDTO updateUserRequest) {
        if(updateUserRequest.handle() != null) validateHandle(updateUserRequest.handle());
        updateRequestMapper.updateUserFromRequest(updateUserRequest, userEntity);
        userRepository.save(userEntity);
        log.info("User {} updated!", userEntity.getEmail());
    }

    public void validateHandle(String handle) {
        if(handle.isBlank() || handle.length() > 20) {
            throw new IllegalHandleSizeException(1, 20);
        }
        else if(userRepository.existsByHandle(handle)) {
            throw new HandleAlreadyTakenException(handle);
        }
        else if(!handle.matches("[a-z0-9]+")){
            throw new InvalidHandleCharactersException();
        }
    }

    private String generateHandle(String displayName) {
        String base = displayName.toLowerCase().replaceAll("[^a-z0-9]", "");

        if(base.isEmpty()) base = "id";

        String handle = base.length() > 20 ? base.substring(0, 20) : base;
        int attempt = 1;

        while (userRepository.existsByHandle(handle)) {
            String suffix = String.valueOf(attempt);
            if (base.length() + suffix.length() > 20) {
                handle = base.substring(0, 20 - suffix.length()) + suffix;
            } else {
                handle = base + suffix;
            }
            attempt++;
        }

        log.debug("Generated handle: {}", handle);
        return handle;
    }
}
