package com.shopping_cart_project.shopping_cart_project.Repository;

import com.shopping_cart_project.shopping_cart_project.Entity.Cart;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository   extends JpaRepository<Cart,Long> {
    //根據用戶的id找尋用戶的購物車的功能
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId ")
    public Cart findCartByUserId(@Param("userId")long userId);
}
