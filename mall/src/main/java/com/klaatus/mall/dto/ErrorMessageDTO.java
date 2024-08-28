package com.klaatus.mall.dto;

import lombok.*;

@Data
@Builder
@ToString
public class ErrorMessageDTO {

    private String code;

    private String message;
}
