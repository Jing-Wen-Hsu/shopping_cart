package com.shopping_cart_project.shopping_cart_project.Response;

//回應的訊息有JWT及MESSAGE
public class AuthResponse {
    private String jwt;
    private String message;
    //無參數建構式
    public AuthResponse() {
    }
    //有參數建構式
    public AuthResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }
    //getter/setter

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
