package io.github.zapolyarnydev.authservice.service;

import io.github.zapolyarnydev.authservice.entity.AuthUser;
import io.github.zapolyarnydev.authservice.exception.EmailNotFoundException;
import io.github.zapolyarnydev.authservice.exception.InvalidCredentialsException;
import io.github.zapolyarnydev.authservice.exception.UserNotFoundException;
import io.github.zapolyarnydev.authservice.repository.AuthUserRepository;
import io.github.zapolyarnydev.commons.events.UserCreatedEvent;
import io.github.zapolyarnydev.commons.exception.EmailAlreadyUsedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    @Transactional
    public AuthUser register(String email, String password) {
        if(userRepository.existsByEmail(email)) throw new EmailAlreadyUsedException(email);

        String hashedPassword = passwordEncoder.encode(password);
        var authUser = new AuthUser(email, hashedPassword);

        authUser = userRepository.saveAndFlush(authUser);

        var event = new UserCreatedEvent(email, authUser.getRegisteredAt());
        kafkaTemplate.send("users.created", event);
        log.info("User registered successfully, event sent to kafka broker. Email: {}", email);
        return authUser;
    }

    public AuthUser authenticate(String email, String password) throws InvalidCredentialsException {
        var authUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        if(!passwordEncoder.matches(password, authUser.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return authUser;
    }

    public AuthUser findUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
