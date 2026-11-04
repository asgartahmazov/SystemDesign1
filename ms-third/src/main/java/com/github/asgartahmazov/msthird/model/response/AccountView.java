package com.github.asgartahmazov.msthird.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountView(UUID id,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          Long version,
                          BigDecimal balance,
                          String description) {
}
