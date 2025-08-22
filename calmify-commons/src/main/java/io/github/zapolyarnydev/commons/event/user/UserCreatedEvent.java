package io.github.zapolyarnydev.commons.event.user;

import java.time.Instant;

public record UserCreatedEvent(String email, Instant registredAt) {
}
