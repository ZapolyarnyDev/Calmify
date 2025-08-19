package io.github.zapolyarnydev.userservice.service;

import io.github.zapolyarnydev.commons.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.commons.exception.UserNotFoundException;
import io.github.zapolyarnydev.userservice.dto.UpdateUserRequestDTO;
import io.github.zapolyarnydev.userservice.dto.UserInfoResponseDTO;
import io.github.zapolyarnydev.userservice.entity.UserEntity;
import io.github.zapolyarnydev.userservice.exception.UserNotFoundByHandleException;
import io.github.zapolyarnydev.userservice.mapper.UserMapper;
import io.github.zapolyarnydev.userservice.repository.UserRepository;
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

    private final HandleService handleService;

    private final UserMapper updateRequestMapper;

    @Transactional
    public UserEntity createUser(String email, Instant registeredAt) throws EmailAlreadyUsedException {
        if(userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        String handle = handleService.generateHandle(email.split("@")[0]);

        var entity = new UserEntity(email, registeredAt);
        entity.setHandle(handle);

        log.info("User created! Email: {}, Handle: {}", email, handle);
        return userRepository.save(entity);
    }

    public UserEntity findUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public UserEntity findUserByHandle(String handle) throws UserNotFoundByHandleException {
        return userRepository.findByHandle(handle)
                .orElseThrow(() -> new UserNotFoundByHandleException(handle));
    }

    @Transactional
    public void updateUser(UserEntity userEntity, UpdateUserRequestDTO updateUserRequest) {
        if(updateUserRequest.handle() != null) handleService.validateHandle(updateUserRequest.handle());
        updateRequestMapper.updateUserFromRequest(updateUserRequest, userEntity);
        userRepository.save(userEntity);
        log.debug("User {} updated!", userEntity.getEmail());
    }

    public UserInfoResponseDTO getUserInfoResponse(String handle) {
        UserEntity entity = findUserByHandle(handle);

        return new UserInfoResponseDTO(
                entity.getDisplayName(),
                handle,
                entity.getDescription(),
                entity.getLastSeenAt()
        );
    }

}
