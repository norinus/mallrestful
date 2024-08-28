package com.klaatus.mall.mapper;

import com.klaatus.mall.domain.ErrorMessage;
import com.klaatus.mall.dto.ErrorMessageDTO;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface ErrorMessageMapper {

    ErrorMessageDTO toDTO(ErrorMessage errorMessage);

    ErrorMessage toEntity(ErrorMessageDTO errorMessageDTO);
}
