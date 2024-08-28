package com.klaatus.mall.controller;

import com.klaatus.mall.dto.ErrorMessageDTO;
import com.klaatus.mall.dto.ProductDTO;
import com.klaatus.mall.service.ErrorMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequestMapping("api/errors")
@RestController
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ErrorMessageController {

    private final ErrorMessageService errorMessageService;
    /**
     * 에러 메시지 등록
     * @param ErrorMessageDTO
     * @return
     * @throws IOException
     */
    @PostMapping("/")
    public Map<String, String> create(ErrorMessageDTO errorMessageDTO) throws IOException {
        return Map.of("code",errorMessageService.setErrorMessage(errorMessageDTO));
    }

    /**
     * 에러메시지 읽기
     * @param code
     * @return
     * @throws IOException
     */
    @GetMapping("/{code}")
    public Map<String, String> getErrorMessage(@PathVariable String code) throws IOException {
        return Map.of("message",errorMessageService.getErrorMessage(code));
    }

    /**
     * 에러 메시지 수정
     * @param code
     * @param errorMessageDTO
     * @return
     * @throws IOException
     */
    @PutMapping("/{code}")
    public Map<String, String> getErrorMessage(@PathVariable String code,ErrorMessageDTO errorMessageDTO) throws IOException {
        errorMessageService.updateMessages(code,errorMessageDTO);
        return Map.of("message","success");
    }

    /**
     * 에러 메시지 삭제
     * @param code
     * @return
     * @throws IOException
     */
    @DeleteMapping("/{code}")
    public Map<String, String> deleteErrorMessage(@PathVariable String code) throws IOException {

        errorMessageService.deleteErrorMessage(code);

        return Map.of("message","success");
    }
    /**
     * 에러 메시지 목록
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public Page<ErrorMessageDTO> list(@PageableDefault(size = 10, sort = "code", direction = Sort.Direction.DESC) Pageable pageable) {
        return errorMessageService.list(pageable);
    }
}
