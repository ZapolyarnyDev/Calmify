package io.github.zapolyarnydev.videoservice.config;

import io.github.zapolyarnydev.videoservice.repository.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class GeneratorConfig {

    private final VideoMetadataRepository metadataRepository;

    @Bean
    public Function<String, Boolean> shortIdExistChecker() {
        return metadataRepository::existsByShortId;
    }
}
