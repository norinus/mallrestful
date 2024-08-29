package com.klaatus.mall.controller;

import com.klaatus.mall.dto.ProductDTO;
import com.klaatus.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequestMapping("api/products")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록
     * @param productDTO
     * @return
     * @throws IOException
     */
    @PostMapping("/")
    public Map<String, Long> create(ProductDTO productDTO) throws IOException {
        return Map.of("pno",productService.create(productDTO));
    }

    /**
     * 상품 읽기
     * @param pno
     * @return
     */
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable Long pno) {
        return productService.read(pno);
    }

    /**
     * 상품 업데이트
     * @param pno
     * @param productDTO
     * @return
     * @throws IOException
     */
    @PutMapping("/{pno}")
    public Map<String, String> update(@PathVariable Long pno,ProductDTO productDTO) throws IOException {
        productService.update(pno, productDTO);
        return Map.of("result","SUCCESS");
    }

    /**
     * 상품 삭제
     * @param pno
     * @return
     */
    @DeleteMapping("/{pno}")
    public Map<String, String> delete(@PathVariable Long pno) {
        productService.delete(pno);
        return Map.of("result", "SUCCESS");
    }

    /**
     * 상품이미지 보기
     * @param filename
     * @return
     * @throws IOException
     */
    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) throws IOException {
        return productService.getFile(filename);
    }

    /**
     * 상품 리스트
     * @param pageable
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public Page<ProductDTO> list(@PageableDefault(size = 10, sort = "pno", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.list(pageable);
    }
}
