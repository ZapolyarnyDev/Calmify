package io.github.zapolyarnydev.userservice.kafka;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandler {

    @Bean
    public DefaultErrorHandler specificRecordErrorHandler(KafkaTemplate<String, SpecificRecord> kafkaTemplate){
        var recover = new DeadLetterPublishingRecoverer(
            kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".dlt", record.partition()
        ));

        return new DefaultErrorHandler(recover, new FixedBackOff(0L, 0L));
    }
}
