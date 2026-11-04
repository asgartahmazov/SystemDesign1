package com.github.asgartahmazov.msthird.model.request;

import java.math.BigDecimal;

public record CreateAccountDto(BigDecimal balance, String description) {
}
