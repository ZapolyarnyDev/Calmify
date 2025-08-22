package io.github.zapolyarnydev.userservice.listener;

import io.github.zapolyarnydev.commons.event.user.UserCreatedEvent;
import io.github.zapolyarnydev.commons.exception.EmailAlreadyUsedException;
import io.github.zapolyarnydev.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @KafkaListener(topics = "users.created",
    containerFactory = "specificRecordContainerFactory")
    public void onUserCreate(UserCreatedEvent event) {
        log.info("User creation event received. Email: {}", event.email());
        var email = event.email();
        Instant registeredAt = event.registredAt();
        try {
            userService.createUser(email, registeredAt);
            log.info("User successfully created from event: {}", email);
        } catch (EmailAlreadyUsedException e) {
            log.warn("Duplicate user creation event for email: {}", event.email());
        } catch (Exception e) {
            log.error("Failed to process event {}, sending to DLT", event, e);
        }
    }
}
