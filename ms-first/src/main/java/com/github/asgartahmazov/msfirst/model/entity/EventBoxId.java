package com.github.asgartahmazov.msfirst.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class EventBoxId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private boolean direction;

    public EventBoxId() {
    }

    public EventBoxId(final UUID id, final boolean direction) {
        this.id = id;
        this.direction = direction;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventBoxId other)) {
            return false;
        }
        return direction == other.direction && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, direction);
    }
}
