package com.github.asgartahmazov.msfirst.model.eventpayload;

import java.math.BigDecimal;

public record AccountCreatedEventPayload(BigDecimal balance, String description) {
}
