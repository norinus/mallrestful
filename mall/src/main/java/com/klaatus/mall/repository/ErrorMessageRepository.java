package com.klaatus.mall.repository;

import com.klaatus.mall.domain.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, String> {
}
