package com.github.asgartahmazov.mssecond.model.request;

import java.math.BigDecimal;

public record CreateAccountDto(BigDecimal balance, String description) {
}
