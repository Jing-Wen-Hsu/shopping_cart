package com.shopping_cart_project.shopping_cart_project.Entity;

import jakarta.persistence.*;

//用戶:id、email(帳號)、密碼
@Entity
@Table(name = "users")
public class User {
    @Id //id設為主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增長
    private Long id;
    private String email;
    private String password;


    public User() {

    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
