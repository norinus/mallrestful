package com.klaatus.mall.service;

import com.klaatus.mall.domain.Product;
import com.klaatus.mall.domain.ProductImage;
import com.klaatus.mall.dto.ProductDTO;
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
    public ProductDTO read(Long pno) {
        return toDTO(productRepository.findByPno(pno).orElseThrow());
    }

    @Override
    public void update(Long pno, ProductDTO productDTO) throws IOException {

        ProductDTO oldProductDTO = toDTO(productRepository.selectOne(pno).orElseThrow());

        List<String> oldUploadFileNames = oldProductDTO.getUploadFileNames();

        List<MultipartFile> files = productDTO.getFiles();

        List<String> currentUploadFileNames = customFileUtil.saveFiles(files);

        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if(currentUploadFileNames!=null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productRepository.findById(pno).ifPresent(product -> {
            product.changePrice(productDTO.getPrice());
            product.changePName(productDTO.getPname());
            product.changeDescription(productDTO.getDescription());
            product.clearList();

            List<String> uploadFileNames = productDTO.getUploadFileNames();

            if(uploadFileNames != null && !uploadFileNames.isEmpty()) {
                uploadFileNames.forEach(product::addImageString);
            }

            productRepository.save(product);
        });

        if(oldUploadFileNames!=null && !oldUploadFileNames.isEmpty()) {
            List<String> removeFiles  = oldUploadFileNames.stream().filter(fileName-> !uploadedFileNames.contains(fileName)).toList();
            customFileUtil.deleteFile(removeFiles);
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
    public Page<ProductDTO> list(Pageable pageable) {
        return productRepository.findAllByIsDeletedFalse(pageable).map(element ->

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
     *ENTITY TO  DTO
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
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        productDTO.setUploadFileNames(imageList.stream().map(ProductImage::getFileName).toList());

        return productDTO;
    }

}
