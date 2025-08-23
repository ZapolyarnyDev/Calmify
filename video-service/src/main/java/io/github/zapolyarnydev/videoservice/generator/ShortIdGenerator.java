package io.github.zapolyarnydev.videoservice.generator;

import io.github.zapolyarnydev.commons.generation.CheckingGenerator;
import io.github.zapolyarnydev.videoservice.util.ShortIdDataProvider;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ShortIdGenerator extends CheckingGenerator<String> {

    public ShortIdGenerator(ShortIdDataProvider dataProvider, Function<String, Boolean> shortIdExistChecker) {
        super(dataProvider, shortIdExistChecker);
    }
}
