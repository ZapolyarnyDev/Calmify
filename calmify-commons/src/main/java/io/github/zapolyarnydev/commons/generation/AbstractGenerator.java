package io.github.zapolyarnydev.commons.generation;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public abstract class AbstractGenerator<T> implements Supplier<T> {

    private final Supplier<T> dataProvider;

    public AbstractGenerator(Supplier<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public abstract T get();

}
