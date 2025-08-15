package io.github.zapolyarnydev.authservice.kafka;

import io.github.zapolyarnydev.commons.avro.AvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic userCreated() {
        return TopicBuilder.name("users.created")
                .replicas(1)
                .partitions(2)
                .build();
    }

    @Bean
    public ProducerFactory<String, SpecificRecord> specificRecordProducerFactory() {
        var configProperties = new HashMap<>(kafkaProperties.buildProducerProperties());

        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProperties);
    }

    @Bean
    public KafkaTemplate<String, SpecificRecord> userCreatedKafkaTemplate() {
        return new KafkaTemplate<>(specificRecordProducerFactory());
    }
}
