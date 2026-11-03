package com.github.asgartahmazov.mssecond.model.eventpayload;

import java.math.BigDecimal;

public record AccountCreatedEventPayload(BigDecimal balance, String description) {
}
