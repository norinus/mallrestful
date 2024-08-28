package com.klaatus.mall.service;

import com.klaatus.mall.domain.Product;
import com.klaatus.mall.domain.ProductImage;
import com.klaatus.mall.dto.ProductDTO;
import com.klaatus.mall.exception.ProductNotFoundException;
import com.klaatus.mall.repository.MemberRepository;
import com.klaatus.mall.repository.ProductRepository;
import com.klaatus.mall.utils.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CustomFileUtil customFileUtil;

    private final MemberRepository memberRepository;

    @Override
    public Long create(ProductDTO productDTO) throws IOException {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = customFileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info("업로드 파일 이름:{}", uploadFileNames.toString());

        return productRepository.save(toEntity(productDTO)).getPno();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO read(Long pno) {
        return toDTO(productRepository.findById(pno).orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public void update(Long pno, ProductDTO productDTO) throws IOException {

        ProductDTO oldProductDTO = toDTO(productRepository.findById(pno).orElseThrow(ProductNotFoundException::new));

        List<String> oldUploadFileNames = oldProductDTO.getUploadFileNames();

        List<MultipartFile> files = productDTO.getFiles();

        List<String> currentUploadFileNames = customFileUtil.saveFiles(files);

        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        // pno를 기반으로 Product를 조회하고, Product가 존재하지 않을 경우 예외를 던집니다.
        Product product = productRepository.findById(pno)
                .orElseThrow(ProductNotFoundException::new); // 적절한 예외 처리 필요

        // Product의 속성들을 업데이트합니다.
        product.changePrice(productDTO.getPrice());
        product.changePName(productDTO.getPname());
        product.changeDescription(productDTO.getDescription());

        // 기존의 이미지 리스트를 비우고, 새로운 이미지 파일 이름을 추가합니다.
        product.clearList();
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(product::addImageString);
        }

        // 변경된 Product 객체를 저장합니다.
        productRepository.save(product);

        // 기존 파일과 업로드된 파일을 비교하여 삭제할 파일을 결정합니다.
        if (oldUploadFileNames != null && !oldUploadFileNames.isEmpty()) {
            List<String> removeFiles = oldUploadFileNames.stream()
                    .filter(fileName -> !Objects.requireNonNull(uploadFileNames).contains(fileName))
                    .toList();
            customFileUtil.deleteFile(removeFiles); // 삭제할 파일을 실제로 삭제합니다.
        }
    }

    @Override
    public void delete(Long tno) {
        productRepository.findById(tno).ifPresent(product -> {
            product.changeIsDeleted(true);
            productRepository.save(product);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> list(Pageable pageable) {
        return productRepository.findAll(pageable).map(element ->

                ProductDTO.builder()
                        .pno(element.getPno())
                        .pname(element.getPname())
                        .price(element.getPrice())
                        .description(element.getDescription())
                        .isDeleted(element.getIsDeleted())
                        .uploadFileNames(element.getImageList().stream()
                                .filter(image -> image.getSortNum() == 0)
                                .map(ProductImage::getFileName)
                                .toList())

                        .build()
        );
    }

    @Override
    public ResponseEntity<Resource> getFile(String fileName) {
        return customFileUtil.getFile(fileName);
    }

    /**
     * DTO TO ENTITY
     *
     * @param productDTO
     * @return
     */
    private Product toEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .isDeleted(false)
                .pname(productDTO.getPname())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames == null || uploadFileNames.isEmpty()) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);

        return product;
    }


    /**
     * ENTITY TO  DTO
     *
     * @param product
     * @return
     */
    private ProductDTO toDTO(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .description(product.getDescription())
                .price(product.getPrice())
                .isDeleted(product.getIsDeleted())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        productDTO.setUploadFileNames(imageList.stream().map(ProductImage::getFileName).toList());

        return productDTO;
    }

}
