package com.klaatus.mall.controller;

import com.klaatus.mall.dto.ProductDTO;
import com.klaatus.mall.service.ProductService;
import com.klaatus.mall.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;



@RequestMapping("api/products")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final CustomFileUtil customFileUtil;

    /**
     * 상품등록
     *
     * @param productDTO
     * @return
     */
    @PostMapping("/")
    public Map<String, Long> create(ProductDTO productDTO) throws IOException {

        log.info("상품등록");

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = customFileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info("업로드 파일 이름:{}", uploadFileNames.toString());

        return Map.of("result", productService.create(productDTO));
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable Long pno) {
        return productService.read(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> update(@PathVariable Long pno,ProductDTO productDTO) throws IOException {

        ProductDTO oldProductDTO = productService.read(pno);

        List<String> oldUploadFileNames = oldProductDTO.getUploadFileNames();

        List<MultipartFile> files = productDTO.getFiles();

        List<String> currentUploadFileNames = customFileUtil.saveFiles(files);

        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if(currentUploadFileNames!=null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.update(pno, productDTO);

        if(oldUploadFileNames!=null && !oldUploadFileNames.isEmpty()) {
            List<String> removeFiles  = oldUploadFileNames.stream().filter(fileName-> !uploadedFileNames.contains(fileName)).toList();
            customFileUtil.deleteFile(removeFiles);
        }

        return Map.of("RESULT","SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> delete(@PathVariable Long pno) {
        productService.delete(pno);
        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) throws IOException {
        return customFileUtil.getFile(filename);
    }

    @GetMapping("/list")
    public Page<ProductDTO> list(@PageableDefault(size = 10, sort = "tno", direction = Sort.Direction.DESC) Pageable pageable) {
        return productService.list(pageable);
    }
}
