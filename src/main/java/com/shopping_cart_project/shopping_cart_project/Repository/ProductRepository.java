package com.shopping_cart_project.shopping_cart_project.Repository;

import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {
}
