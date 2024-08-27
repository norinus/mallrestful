package com.klaatus.mall.service;

import com.klaatus.mall.domain.Todo;
import com.klaatus.mall.dto.TodoDTO;
import com.klaatus.mall.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    private final ModelMapper modelMapper;


    @Override
    public Long create(TodoDTO todoDTO) {
        return repository.save(modelMapper.map(todoDTO, Todo.class)).getTno();
    }

    @Override
    @Transactional(readOnly = true)
    public TodoDTO read(Long tno) {
        return repository.findById(tno).map((element) -> modelMapper.map(element, TodoDTO.class)).get();
    }

    @Override
    public void update(Long tno,TodoDTO todoDTO) {
        repository.findById(tno).ifPresent(entity -> {
            repository.save(modelMapper.map(todoDTO , Todo.class));
        });
    }

    @Override
    public void delete(Long tno) {
        repository.findById(tno).ifPresent(entity -> {
            repository.deleteById(tno);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map((element) -> modelMapper.map(element, TodoDTO.class));
    }
}
