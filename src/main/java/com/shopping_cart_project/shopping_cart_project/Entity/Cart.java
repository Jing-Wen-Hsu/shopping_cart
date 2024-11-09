package com.shopping_cart_project.shopping_cart_project.Entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //購物車與用戶關聯，代表一對多的關係，因為一個購物車裡面可以有很多物品。
    @JoinColumn(name = "user_id",nullable = false)
    //@JoinColumn表示我們會在Cart的資料表中，多增加一列用來儲存用戶id的數值，這一列的名字是user_id，不能是null。
    private User user;

    @OneToMany (mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();
    //mappedBy = "cart”   表示cart這邊不去處理與CartItem間的關聯，而是交給CartItem處理關聯。
    //因此，cart資料表不會出現cartItem_id的欄位。
    //CascadeType.ALL  表示對Cart entity的任何操作都會傳播到CartItem entity。目的是清空購物車時，購物車中的物品也一併刪除。
    //orphanRemoval = true    如果我們在cart刪除購物車物品，那麼cartItem的內容也會被刪除。

    private Integer totalPrice;
    private Integer totalQuantity;

    public Cart() {
    }

    public Cart(Long id, User user, Set<CartItem> cartItems, Integer totalPrice, Integer totalQuantity) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
