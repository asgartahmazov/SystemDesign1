package com.github.asgartahmazov.msfirst.repository;

import com.github.asgartahmazov.msfirst.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
}
