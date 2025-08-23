package io.github.zapolyarnydev.videoservice.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.github.zapolyarnydev.commons.constants.GenerationConstants;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ShortIdDataProvider implements Supplier<String> {

    @Override
    public String get() {
        char[] alphabet = GenerationConstants.ALPHABET.toCharArray();
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabet, 10);
    }
}
