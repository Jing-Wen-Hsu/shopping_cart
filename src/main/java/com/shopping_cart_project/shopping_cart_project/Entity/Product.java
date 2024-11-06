package com.shopping_cart_project.shopping_cart_project.Entity;

import jakarta.persistence.*;

//商品id、商品名稱、商品描述、商品價格、商品圖片、商品種類
@Entity
@Table(name = "products") //對應資料庫的products表
public class Product {

    //以下變數對應products表裡的colume
    @Id //設為主建
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增長
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String image;
    private String category;
    //Generate出建構式、getter/setter

    public Product() {
    }

    public Product(Long id, String name, String description, Integer price, String image, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
