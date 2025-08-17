package io.github.zapolyarnydev.authservice.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Bean
    public NewTopic userCreated() {
        return TopicBuilder.name("users.created")
                .replicas(1)
                .partitions(2)
                .build();
    }
}
