package io.github.zapolyarnydev.userservice.service;

import io.github.zapolyarnydev.userservice.exception.HandleAlreadyTakenException;
import io.github.zapolyarnydev.userservice.exception.IllegalHandleSizeException;
import io.github.zapolyarnydev.userservice.exception.InvalidHandleCharactersException;
import io.github.zapolyarnydev.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class HandleService {

    private final UserRepository userRepository;

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

    public String generateHandle(String displayName) {
        String base = displayName.toLowerCase().replaceAll("[^a-z0-9]", "");

        if (base.isEmpty()) {
            base = "user" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        }

        String handle = base.length() > 20 ? base.substring(0, 20) : base;

        while (userRepository.existsByHandle(handle)) {
            String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
            if (base.length() + suffix.length() > 20) {
                handle = base.substring(0, 20 - suffix.length()) + suffix;
            } else {
                handle = base + suffix;
            }
        }

        log.debug("Generated handle: {}", handle);
        return handle;
    }

}
