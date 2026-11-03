package com.github.asgartahmazov.mssecond.repository;

import com.github.asgartahmazov.mssecond.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
}
