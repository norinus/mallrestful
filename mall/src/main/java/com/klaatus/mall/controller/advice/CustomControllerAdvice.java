package com.klaatus.mall.controller.advice;

import com.klaatus.mall.utils.CustomJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomControllerAdvice {

    //컬렉션에서 요소를 가져오려고 할 때, 해당 요소가 없는 경우에 발생하는 예외
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handleNoSuchElementException(NoSuchElementException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg",message));

    }

    //요청 인자가 유효하지 않을 때 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg",message));
    }

    //산술 연산 중에 발생하는 예외
    @ExceptionHandler(ArithmeticException.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(ArithmeticException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg",message));
    }

    //메서드 호출 시 인자로 전달되는 값 자체가 잘못된 경우
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgument(IllegalArgumentException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg",message));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAllException(Exception exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg",message));
    }

    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<?> handleCustomJWTException(CustomJWTException exception) {
        String message = exception.getMessage();
        return ResponseEntity.ok().body(Map.of("error",message));
    }
}
