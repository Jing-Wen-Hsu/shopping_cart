package com.shopping_cart_project.shopping_cart_project.Request;

//定義加入購物車的請求的格式
public class AddItemRequest {
    private Long productId;
    private Integer quantity;

    public AddItemRequest() {
    }

    public AddItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
