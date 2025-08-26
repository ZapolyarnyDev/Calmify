package io.github.zapolyarnydev.engagement.domain;

import io.github.zapolyarnydev.engagement.domain.base.Authorship;
import io.github.zapolyarnydev.engagement.domain.type.RateType;

/**
 * @param <I> The type of engagement target entity ID
 * @param <T> The type of target ID
 * @param <A> The type of author id
 * */
public interface Rate<I, T, A> extends EngagementTarget<I, T>, Authorship<A> {

    RateType getRate();

    void changeRate(RateType rateType);

}
