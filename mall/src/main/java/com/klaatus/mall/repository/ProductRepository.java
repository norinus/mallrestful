package com.klaatus.mall.repository;

import com.klaatus.mall.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @EntityGraph(attributePaths = "imageList")
    Optional<Product> findByPno(Long pno);

    @EntityGraph(attributePaths = "imageList")
    Page<Product> findAllByIsDeletedFalse(Pageable pageable);

    //사용안함
    @Deprecated
    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p where p.pno= :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    //사용안함
    @Deprecated
    @Modifying
    @Query("update Product p set p.isDeleted=:isDeleted where p.pno=:pno")
    void updateToDelete(@Param("pno") Long pno, @Param("isDeleted") boolean isDeleted);

    //사용안함
    @Deprecated
    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p left join p.imageList pi where pi.sortNum = 0 and p.isDeleted = false ")
    Page<Product> getList(Pageable pageable);

}
