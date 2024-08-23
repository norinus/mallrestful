package com.klaatus.mall.controller;

import com.klaatus.mall.dto.TodoDTO;
import com.klaatus.mall.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("api/todo")
@RequiredArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/")
    public Map<String, Long> create(@RequestBody TodoDTO todoDTO) {
        return Map.of("TNO", todoService.create(todoDTO));
    }

    @GetMapping("/{tno}")
    public TodoDTO read(@PathVariable Long tno) {
        return todoService.read(tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> update(@PathVariable Long tno, @RequestBody TodoDTO todoDTO) {
        todoService.update(tno, todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> delete(@PathVariable Long tno) {
        todoService.delete(tno);
        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/list")
    public Page<TodoDTO> list(@PageableDefault(size = 10, sort = "tno", direction = Sort.Direction.DESC)Pageable pageable) {
        return todoService.list(pageable);
    }

}
