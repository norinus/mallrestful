package com.klaatus.mall.exception;

import com.klaatus.mall.service.ErrorMessageService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}