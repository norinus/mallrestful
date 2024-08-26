package com.klaatus.mall.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    private Integer sortNum;

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

}
