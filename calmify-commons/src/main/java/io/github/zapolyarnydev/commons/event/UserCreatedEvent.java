package io.github.zapolyarnydev.commons.event;

import java.time.Instant;

public record UserCreatedEvent(String email, Instant registredAt) {
}
