package com.github.asgartahmazov.msthird.repository;

import com.github.asgartahmazov.msthird.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
}
