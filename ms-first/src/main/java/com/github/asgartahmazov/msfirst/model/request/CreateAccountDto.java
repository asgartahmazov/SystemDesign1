package com.github.asgartahmazov.msfirst.model.request;

import java.math.BigDecimal;

public record CreateAccountDto(BigDecimal balance, String description) {
}
