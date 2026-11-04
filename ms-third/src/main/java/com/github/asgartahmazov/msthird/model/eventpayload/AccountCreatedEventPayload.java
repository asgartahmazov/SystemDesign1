package com.github.asgartahmazov.msthird.model.eventpayload;

import java.math.BigDecimal;

public record AccountCreatedEventPayload(BigDecimal balance, String description) {
}
