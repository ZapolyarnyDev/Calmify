package io.github.zapolyarnydev.engagement.domain;

import io.github.zapolyarnydev.engagement.domain.base.Authorship;
import io.github.zapolyarnydev.engagement.domain.base.TemporalInfo;

import java.time.Instant;

/**
 * @param <I> The type of engagement target entity ID
 * @param <T> The type of target ID
 * @param <A> The type of author id
 * */
public interface Comment<I, T, A> extends Engagement<I, T>, Target, Authorship<A>, TemporalInfo<Instant> {

    String getText();

    void editText(String newText);

}
