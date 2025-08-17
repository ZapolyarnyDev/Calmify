package io.github.zapolyarnydev.userservice.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaTemplate<String, SpecificRecord> kafkaTemplate;

    @Bean
    public org.apache.kafka.clients.admin.NewTopic usersCreatedDLT() {
        return TopicBuilder.name("users.created.dlt")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, SpecificRecord> specificRecordConsumerFactory(KafkaProperties kafkaProperties) {
        var props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("specific.avro.reader", true);
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecord> specificRecordContainerFactory(
            KafkaTemplate<String, SpecificRecord> kafkaTemplate,
            ConsumerFactory<String, SpecificRecord> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, SpecificRecord>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(2);

        var errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) -> new TopicPartition(record.topic() + ".dlt", record.partition())
                ),
                new FixedBackOff(0L, 0L)
        );

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }


}
