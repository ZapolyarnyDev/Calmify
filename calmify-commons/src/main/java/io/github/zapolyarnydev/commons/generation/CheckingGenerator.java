package io.github.zapolyarnydev.commons.generation;

import java.util.function.Function;
import java.util.function.Supplier;

public class CheckingGenerator<T> extends AbstractGenerator<T> {

    private final Function<T, Boolean> dataFitsChecker;

    public CheckingGenerator(Supplier<T> dataProvider, Function<T, Boolean> dataFitsChecker) {
        super(dataProvider);
        this.dataFitsChecker = dataFitsChecker;
    }

    @Override
    public T get() {
        while (true) {
            T data = getDataProvider().get();
            if (dataFitsChecker.apply(data)) {
                return data;
            }
        }
    }
}
