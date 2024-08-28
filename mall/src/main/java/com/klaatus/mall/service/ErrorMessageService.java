package com.klaatus.mall.service;


import com.klaatus.mall.dto.ErrorMessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ErrorMessageService {
    public String getErrorMessage(String code);
    public String setErrorMessage(ErrorMessageDTO errorMessageDTO);

    public void deleteErrorMessage(String code);

    public void updateMessages(String code, ErrorMessageDTO errorMessageDTO);

    public Page<ErrorMessageDTO> list(Pageable pageable);
}
