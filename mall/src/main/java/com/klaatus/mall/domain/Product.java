package com.klaatus.mall.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private  String pname;

    private Integer price;

    private String description;

    @Builder.Default
    private Boolean isDeleted = false; // Default value set here


    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void changePName(String pname) {
        this.pname = pname;
    }

    public void changePrice(Integer price){
        this.price = price;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void addImage(ProductImage image){

        image.setSortNum(this.imageList.size());
        this.imageList.add(image);
    }

    public void addImageString(String imageFileName){
        addImage(ProductImage.builder().fileName(imageFileName).build());
    }

    public void clearList(){
        this.imageList.clear();
    }

}
