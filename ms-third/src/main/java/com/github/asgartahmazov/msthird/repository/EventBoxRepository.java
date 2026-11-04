package com.github.asgartahmazov.msthird.repository;

import com.github.asgartahmazov.msthird.model.entity.EventBoxEntity;
import com.github.asgartahmazov.msthird.model.entity.EventBoxId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventBoxRepository extends JpaRepository<EventBoxEntity, EventBoxId> {

    @Query(value = """
            SELECT * FROM eventbox
            WHERE direction = true
              AND processed_at IS NULL
              AND retry_count < :maxRetries
            ORDER BY created_at
            LIMIT :batchSize
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<EventBoxEntity> findOutboxBatch(int batchSize, int maxRetries);

    @Modifying
    @Query(value = """
        INSERT INTO eventbox (id, direction, topic, key, payload, headers, processed_at)
        VALUES (:id, false, :topic, :key, CAST(:payload AS jsonb), CAST(:headers AS jsonb), now())
        ON CONFLICT (id, direction) DO NOTHING
        """, nativeQuery = true)
    int insertInboxIfAbsent(UUID id, String topic, String key, String payload, String headers);
}
