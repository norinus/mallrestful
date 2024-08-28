package com.klaatus.mall.repository;

import com.klaatus.mall.domain.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ErrorMessageRepositoryTest {


    @Autowired
    private ErrorMessageRepository repository;

    @Test
    void insert() {

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode("NOT_FOUND");
        errorMessage.setMessage("정보를 찾을 수 없습니다.");

        repository.save(errorMessage);

    }
}
