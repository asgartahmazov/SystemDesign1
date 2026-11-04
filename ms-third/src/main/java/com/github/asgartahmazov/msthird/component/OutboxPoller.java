package com.github.asgartahmazov.msthird.component;

import com.github.asgartahmazov.msthird.model.entity.EventBoxEntity;
import com.github.asgartahmazov.msthird.repository.EventBoxRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxPoller {

    private static final int BATCH_SIZE = 100;
    private static final int MAX_RETRIES = 5;

    private final EventBoxRepository eventBoxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPoller(final EventBoxRepository eventBoxRepository,
                        final KafkaTemplate<String, String> kafkaTemplate) {
        this.eventBoxRepository = eventBoxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void poll() {
        final List<EventBoxEntity> events = eventBoxRepository.findOutboxBatch(BATCH_SIZE, MAX_RETRIES);
        for (final EventBoxEntity event : events) {
            try {
                kafkaTemplate.send(toRecord(event)).get(10, TimeUnit.SECONDS);
                event.setProcessedAt(LocalDateTime.now());
                event.setError(null);
            } catch (final Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                event.setError(e.getMessage());
            }
        }
    }

    private ProducerRecord<String, String> toRecord(final EventBoxEntity event) {
        final ProducerRecord<String, String> record =
                new ProducerRecord<>(event.getTopic(), event.getKey(), event.getPayload());
        event.getHeaders().forEach((name, value) ->
                record.headers().add(name, value.getBytes(StandardCharsets.UTF_8)));
        record.headers().add("eventId", event.getId().toString().getBytes(StandardCharsets.UTF_8));
        return record;
    }
}
