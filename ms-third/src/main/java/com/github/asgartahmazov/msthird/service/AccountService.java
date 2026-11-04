package com.github.asgartahmazov.msthird.service;

import com.github.asgartahmazov.msthird.model.constant.KafkaConstant;
import com.github.asgartahmazov.msthird.model.entity.AccountEntity;
import com.github.asgartahmazov.msthird.model.entity.EventBoxEntity;
import com.github.asgartahmazov.msthird.model.eventpayload.AccountCreatedEventPayload;
import com.github.asgartahmazov.msthird.model.request.CreateAccountDto;
import com.github.asgartahmazov.msthird.repository.AccountRepository;
import com.github.asgartahmazov.msthird.repository.EventBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EventBoxRepository eventBoxRepository;

    private final ObjectMapper objectMapper;

    public AccountService(final AccountRepository accountRepository,
                          final EventBoxRepository eventBoxRepository,
                          final ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.eventBoxRepository = eventBoxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void create(final CreateAccountDto dto) {

        EventBoxEntity eventBox = new EventBoxEntity();
        eventBox.setId(UUID.randomUUID());
        eventBox.setDirection(true);
        eventBox.setTopic(KafkaConstant.TopicName.ACCOUNT_CREATED);
        eventBox.setHeaders(new HashMap<>());
        eventBox.setPayload(objectMapper.writeValueAsString(
                new AccountCreatedEventPayload(
                        dto.balance(), dto.description()
                )
        ));
        eventBoxRepository.save(eventBox);
    }

    @Transactional
    public void createFromEvent(final AccountCreatedEventPayload payload) {
        final AccountEntity account = new AccountEntity();
        account.setBalance(payload.balance());
        account.setDescription(payload.description());
        accountRepository.save(account);
    }
}
