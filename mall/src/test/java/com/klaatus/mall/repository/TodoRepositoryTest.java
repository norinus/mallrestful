package com.klaatus.mall.repository;


import com.klaatus.mall.domain.Todo;
import com.klaatus.mall.dto.TodoDTO;
import com.klaatus.mall.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;


@SpringBootTest
@Slf4j
public class TodoRepositoryTest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TodoRepository repository;

    @Autowired
    TodoService todoService;

    @Test
    void insert() {
        for (int i = 0; i < 100; i++) {
            repository.save(Todo.builder().title("title"+String.valueOf(i)).dueDate(LocalDate.of(2023,12,21)).writer("user"+String.valueOf(i)).build());
        }
    }


    @Test
    void read(){
        Long tno = 3L;
        Todo todo = repository.findById(tno).orElse(new Todo());
        log.info(" 읽은 TODO :{}", todo);
    }

    @Test
    void update() {
        Long tno = 3L;
        repository.findById(tno).ifPresent(
                entity -> {

                    entity.setTitle("test333333");
                    entity.setWriter("update323333");
                    entity.setDueDate(LocalDate.of(2023,12,21));
                    entity.setComplete(true);
                    Todo todo = repository.save(entity);
                }
        );
    }

    @Test
    void paging(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());

        Page<TodoDTO> result = repository.findAll(pageable).map((element) -> modelMapper.map(element, TodoDTO.class));

        result.getContent().stream().forEach(entity->{
            log.info(entity.toString());
        });


        log.info("--------->totalElements: {}",result.getTotalElements());
        log.info("--------->getTotalPages: {}",result.getTotalPages());
        log.info("--------->getNumber: {}",result.getNumber());
        log.info("--------->result.getPageable().getPageNumber: {}",result.getPageable().getPageNumber());
        log.info("--------->result.getPageable().getPageSize: {}",result.getPageable().getPageSize());
        log.info("--------->result.getNumberOfElements: {}",result.getNumberOfElements());
        log.info("--------->result.getSort(): {}",result.getSort());
        log.info("--------->result.isFirst(): {}",result.isFirst());
        log.info("--------->result.isLast(): {}",result.isLast());
        log.info("--------->result.hasNext(): {}",result.hasNext());
        log.info("--------->result.hasPrevious(): {}",result.hasPrevious());
        log.info("--------->result.nextPageable(): {}",result.nextPageable());
        log.info("--------->result.previousPageable(): {}",result.previousPageable());

    }

    @Test
    void update2() {
        Long tno = 3L;
        repository.findById(tno).ifPresent(
                entity -> {
                    entity.setTitle("test333333");
                    entity.setWriter("update323333");
                    entity.setDueDate(LocalDate.of(2023,12,21));
                    entity.setComplete(true);
                    Todo todo = repository.save(entity);
                }
        );
    }
}
