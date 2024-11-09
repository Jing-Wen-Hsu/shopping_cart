package com.shopping_cart_project.shopping_cart_project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cartItems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore //用於避免序列化 cart 字段，防止在 JSON 回傳時出現循環引用問題。 表示不會在JSON輸出中看到這部分的內容。
    private Cart cart;

    @ManyToOne //表示一種商品可以存在於多個購物車項目中。
    private Product product;

    private Integer price;
    private Integer quantity;

    public CartItem() {
    }

    public CartItem(Long id, Cart cart, Product product, Integer price, Integer quantity) {
        this.id = id;
        this.cart = cart;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
