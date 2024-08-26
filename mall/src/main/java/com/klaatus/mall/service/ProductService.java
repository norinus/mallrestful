package com.klaatus.mall.service;

import com.klaatus.mall.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Long create(ProductDTO productDTO);

    ProductDTO read(Long tno);

    void update(Long tno, ProductDTO productDTO);

    void delete(Long tno);

    Page<ProductDTO> list(Pageable pageable);
}
