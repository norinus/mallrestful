package com.klaatus.mall.service;

import com.klaatus.mall.domain.ErrorMessage;
import com.klaatus.mall.dto.ErrorMessageDTO;
import com.klaatus.mall.exception.NotFoundException;
import com.klaatus.mall.exception.ProductNotFoundException;
import com.klaatus.mall.mapper.ErrorMessageMapper;
import com.klaatus.mall.repository.ErrorMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ErrorMessageServiceImpl implements ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;

    private final ErrorMessageMapper errorMessageMapper;


    @Transactional(readOnly = true)
    @Override
    public String getErrorMessage(String code) {
        return errorMessageRepository.findById(code)
                .map(ErrorMessage::getMessage)
                .orElseThrow(NotFoundException::new); // 기본 에러 메시지
    }

    @Override
    public String setErrorMessage(ErrorMessageDTO errorMessageDTO) {
      return errorMessageRepository.save(errorMessageMapper.toEntity(errorMessageDTO)).getCode();
    }

    @Override
    public void deleteErrorMessage(String code) {
        errorMessageRepository.deleteById(code);
    }

    @Override
    public void updateMessages(String code, ErrorMessageDTO errorMessageDTO) {
        errorMessageRepository.findById(code).ifPresent(errorMessage -> {
            errorMessage.setMessage(errorMessageDTO.getMessage());
            errorMessageRepository.save(errorMessage);
        });
    }

    @Override
    public Page<ErrorMessageDTO> list(Pageable pageable) {
        return errorMessageRepository.findAll(pageable).map(errorMessageMapper::toDTO);
    }
}
