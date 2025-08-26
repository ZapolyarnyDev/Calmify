package io.github.zapolyarnydev.engagement.domain.base;

import java.time.temporal.Temporal;

/**
 * @param <T> the type of temporal data (e.g., Instant)
 * */
public interface TemporalInfo<T extends Temporal> {

    T getPublishedAt();

    T getEditedAt();
}
