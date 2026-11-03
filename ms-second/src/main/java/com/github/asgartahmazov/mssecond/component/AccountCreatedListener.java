package com.github.asgartahmazov.mssecond.component;

import com.github.asgartahmazov.mssecond.model.constant.KafkaConstant;
import com.github.asgartahmazov.mssecond.model.eventpayload.AccountCreatedEventPayload;
import com.github.asgartahmazov.mssecond.repository.EventBoxRepository;
import com.github.asgartahmazov.mssecond.service.AccountService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AccountCreatedListener {

    private final EventBoxRepository eventBoxRepository;
    private final AccountService accountService;
    private final JsonMapper jsonMapper;

    public AccountCreatedListener(final EventBoxRepository eventBoxRepository,
                                  final AccountService accountService,
                                  final JsonMapper jsonMapper) {
        this.eventBoxRepository = eventBoxRepository;
        this.accountService = accountService;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = KafkaConstant.TopicName.ACCOUNT_CREATED)
    @Transactional
    public void listen(final ConsumerRecord<String, String> record) {
        final Header eventIdHeader = record.headers().lastHeader("eventId");
        if (eventIdHeader == null) {
            throw new IllegalStateException("eventId header is missing");
        }
        final UUID eventId = UUID.fromString(new String(eventIdHeader.value(), StandardCharsets.UTF_8));

        final Map<String, String> headers = new HashMap<>();
        for (final Header header : record.headers()) {
            headers.put(header.key(), new String(header.value(), StandardCharsets.UTF_8));
        }

        final int inserted = eventBoxRepository.insertInboxIfAbsent(
                eventId, record.topic(), record.key(), record.value(), jsonMapper.writeValueAsString(headers)
        );
        if (inserted == 0) {
            return;
        }

        accountService.createFromEvent(
                jsonMapper.readValue(record.value(), AccountCreatedEventPayload.class)
        );
    }
}
