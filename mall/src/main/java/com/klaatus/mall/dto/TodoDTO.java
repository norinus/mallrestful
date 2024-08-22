package com.klaatus.mall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;


@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long tno;

    private String title;

    private String writer;

    private Boolean complete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}
