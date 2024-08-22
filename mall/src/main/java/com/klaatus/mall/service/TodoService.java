package com.klaatus.mall.service;

import com.klaatus.mall.domain.Todo;
import com.klaatus.mall.dto.TodoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface TodoService {

    public Long create(TodoDTO todoDTO);

    TodoDTO read(Long tno);

    void update(Long tno, TodoDTO todoDTO);

    void delete(Long tno);

    Page<TodoDTO> list(Pageable pageable);
}
