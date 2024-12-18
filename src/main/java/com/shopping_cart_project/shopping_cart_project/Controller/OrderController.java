package com.shopping_cart_project.shopping_cart_project.Controller;


import com.shopping_cart_project.shopping_cart_project.Entity.Order;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Service.CartService;
import com.shopping_cart_project.shopping_cart_project.Service.OrderService;
import com.shopping_cart_project.shopping_cart_project.Service.UserService;
import com.stripe.model.checkout.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    //建立Session，並產生新的訂單
    @GetMapping("/create_session")
    public ResponseEntity<Order> createCheckoutSession(@RequestHeader("Authorization") String jwt) throws Exception {

        //從HTTP請求的Header中接收Authorization字段，獲取JWT。此JWT用來識別請求者的身份，後續會用來驗證用戶並獲取相關資訊。
        //根據 JWT 找到對應的用戶
        User user = userService.findUserByJWT(jwt);
        //獲取用戶 ID
        Long userId = user.getId();
        //清空購物車並計算總金額
        Integer totalPrice = cartService.clearCart(userId);
        //建立 Stripe Checkout Session
            //調用 orderService 的方法，向 Stripe API 發送請求，創建一個 Checkout Session。
        Session session = orderService.createCheckoutSession(totalPrice);
        //建立新的訂單
        Order order = orderService.createOrder(session.getId(), totalPrice, session.getPaymentStatus(), session.getUrl(), userId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    //查詢用戶的全部訂單
    @GetMapping("/find_order")
    public ResponseEntity<List<Order>> findOrderByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
    User user = userService.findUserByJWT(jwt);
    return ResponseEntity.ok(orderService.findOrderByUserId(user.getId()));
    }
}
