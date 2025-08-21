package io.github.zapolyarnydev.userservice.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {


    @Bean
    public org.apache.kafka.clients.admin.NewTopic usersCreatedDLT() {
        return TopicBuilder.name("users.created.dlt")
                .partitions(2)
                .replicas(1)
                .build();
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> specificRecordContainerFactory(
            KafkaTemplate<String, Object> kafkaTemplate,
            ConsumerFactory<String, Object> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
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
