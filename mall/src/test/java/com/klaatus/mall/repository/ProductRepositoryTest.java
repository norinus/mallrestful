package com.klaatus.mall.repository;


import com.klaatus.mall.domain.Product;
import com.klaatus.mall.domain.ProductImage;
import com.klaatus.mall.domain.Todo;
import com.klaatus.mall.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;


    @Test
    public void testInsert(){

        for (int i = 0; i < 10; i++) {

            Product product = Product.builder().pname("상품"+String.valueOf(i)).price(100*i).description("상품설명"+String.valueOf(i)).build();

            product.addImageString(UUID.randomUUID().toString()+"_"+"IMAGE1.jpg");
            product.addImageString(UUID.randomUUID().toString()+"_"+"IMAGE2.jpg");

            repository.save(product);
        }
    }


    @Transactional
    @Test
    void testRead(){
        Long pno = 1L;

        Optional<Product> product = repository.findById(pno);

        Product product1 = product.orElseThrow(RuntimeException::new);

        log.info("상품 정보: {}",product1);
        log.info("상품 이미지 정보: {}",product1.getImageList());
    }

    @Test
    void testRead2(){
        Long pno = 1L;

        Optional<Product> product = repository.selectOne(pno);

        Product product1 = product.orElseThrow(RuntimeException::new);

        log.info("상품 정보: {}",product1);
        log.info("상품 이미지 정보: {}",product1.getImageList());
    }


    @Test
    @Commit
    @Transactional
    void  updateDeleted(){

        Long pno = 2L;
        repository.updateToDelete(pno, true);


    }


    @Test
    void testUpdate(){

        Long pno = 1L;

        Product product = repository.selectOne(pno).orElseThrow(RuntimeException::new);

        product.changePName("1번 상품");
        product.changeDescription("1번 상품설명");
        product.changePrice(5000);

        product.clearList();

        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE3.jpg");

        repository.save(product);


    }


    @Test
    @Transactional(readOnly = true)
    void list(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> productList = repository.findAllByIsDeletedFalse(pageable);

        productList.getContent().forEach(product -> {
            log.info("---------------------------->상품: {}",product.getPno());
            log.info("---------------------------->상품: {}",product.getImageList());
        });

    }
    @Test
    void list2(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> productList = repository.getList(pageable);

        productList.getContent().forEach(product -> {
            log.info("---------------------------->상품: {}",product.getPno());
            log.info("---------------------------->상품: {}",product.getImageList());
        });

    }


    @Test
    void list3() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> productList =  repository.getList(pageable).map((element) -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setPno(element.getPno());
            productDTO.setDescription(element.getDescription());
            productDTO.setPname(element.getPname());
            productDTO.setPrice(element.getPrice());
            productDTO.setUploadFileNames(element.getImageList().stream().map(ProductImage::getFileName).toList());
            return productDTO;
        });

        productList.getContent().forEach(product -> {
            log.info("---------------------------->상품: {}",product.getPno());
            log.info("---------------------------->상품: {}",product.getUploadFileNames());
        });
    }

}
