package com.klaatus.mall.service;

import com.klaatus.mall.dto.ProductDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ProductService {

    Long create(ProductDTO productDTO) throws IOException;

    ProductDTO read(Long tno);

    void update(Long tno, ProductDTO productDTO) throws IOException;

    void delete(Long tno);

    Page<ProductDTO> list(Pageable pageable);

    ResponseEntity<Resource> getFile(String fileName);
}
