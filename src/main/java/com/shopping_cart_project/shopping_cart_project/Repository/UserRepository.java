package com.shopping_cart_project.shopping_cart_project.Repository;

import com.shopping_cart_project.shopping_cart_project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    User findByEmail(String email);
}
