package io.github.zapolyarnydev.engagement.domain;

import io.github.zapolyarnydev.engagement.domain.type.TargetType;

/**
 * @param <I> The type of engagement target entity ID
 * @param <T> The type of target ID
 * */
public interface Engagement<I, T> {

    I getId();

    T getTargetId();
}
