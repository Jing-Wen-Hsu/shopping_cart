package com.shopping_cart_project.shopping_cart_project.Repository;

import com.shopping_cart_project.shopping_cart_project.Entity.Cart;
import com.shopping_cart_project.shopping_cart_project.Entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    //查看購物車，是否已經有同樣的商品了，避免重複加入
@Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product")
    public CartItem isCartItemInCart(@Param("cart") Cart cart, @Param("product")Product product);
}
