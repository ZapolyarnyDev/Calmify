package io.github.zapolyarnydev.userservice.kafka;

import io.github.zapolyarnydev.commons.avro.AvroDeserializer;
import io.github.zapolyarnydev.commons.avro.AvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;
    private final DefaultErrorHandler specificRecordErrorHandler;

    @Bean
    public NewTopic usersCreatedDLT() {
        return TopicBuilder.name("users.created.dlt")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, SpecificRecord> specificRecordConsumerFactory() {
        var configProperties = new HashMap<>(kafkaProperties.buildConsumerProperties());

        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecord> specificRecordContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, SpecificRecord>();
        factory.setConsumerFactory(specificRecordConsumerFactory());

        factory.setConcurrency(2);
        factory.setCommonErrorHandler(specificRecordErrorHandler);

        return factory;
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
