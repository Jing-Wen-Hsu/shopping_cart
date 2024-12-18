package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Order;
import com.shopping_cart_project.shopping_cart_project.Repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    // 建立Stripe支付的session
        //import Session時選擇com.stripe.model.checkout
    public Session createCheckoutSession(int amount) throws StripeException {

        //設定創建Session的參數
        SessionCreateParams params = SessionCreateParams.builder()
                //設定支付模式為單次付款 (PAYMENT)，代表用戶只需支付一次，不會有訂閱或重複扣款
                .setMode(SessionCreateParams.Mode.PAYMENT)
                //成功付款後的跳轉網址
                .setSuccessUrl("http://localhost:5173/checkout/success")

                //建立Stripe的購物車內容
                //添加購物車項目，每個購物車項目（商品）都是一個 LineItem，可以多次使用 .addLineItem 添加多個商品。
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                //設定數量為1個
                                .setQuantity(1L)
                                //設定價格的資料
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                //設定貨幣為新台幣
                                                .setCurrency("twd")
                                                //設定商品價格，為什麼要乘100呢？
                                                //原來是為了國際化，例如美國常常出現$9.99，用浮點數儲存可能有誤差
                                                //為了避免誤差，不如直接乘100，變成整數的999。
                                                //想得知真正的價格時，再除100，就能取得真實的價格
                                                .setUnitAmount(amount * 100L)
                                                //設定商品的資料
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                //設定商品名稱
                                                                .setName("96gen shopping cart product")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        return Session.create(params);
    }


    //建立訂單
    public Order createOrder(String sessionId,Integer totalPrice,String status,String url,Long userId) throws Exception {

        //建立一個訂單空間存放資料
        Order order = new Order();
        //開始放資料
        order.setSessionId(sessionId);
        order.setAmount(totalPrice);  //購物車的totalPrice塞到訂單的Amount
        order.setStatus(status);
        order.setUrl(url);
        order.setUserId(userId);
        //return並且儲存
        return orderRepository.save(order);
    }

    //使用Id查詢用戶訂單資訊，查詢時同時更新資訊 (用戶可能擁有多筆訂單)
    public List<Order> findOrderByUserId(Long userId) throws Exception {
        List<Order> orders = orderRepository.findOrderByUserId(userId);
        //建立一個新的空的 ArrayList，用來儲存更新過的訂單。
        List<Order> updated_orders = new ArrayList<>();
        //遍歷訂單列表並更新每筆訂單
        for (Order order : orders){
            updateOrder(order.getId()); //另有定義一個updateOrder方法
            updated_orders.add(order); //將處理過的訂單物件添加到 updated_orders 列表中。
        }
        return updated_orders;
    }

    //更新訂單資訊的付款狀態
    private void updateOrder(Long id) throws Exception{
        Optional<Order> opt = orderRepository.findById(id);
        //如果這張訂單存在就進行更新
        if (opt.isPresent()){
            //取出這張存在的訂單
            Order updated = opt.get();
            //根據SessionId從Stripe API取得Session資料，因為我們是從這邊付款的
            Session session = Session.retrieve(opt.get().getSessionId());
            //更新付款狀態
            updated.setStatus(session.getPaymentStatus());
            orderRepository.save(updated);
            return;
        }
        throw new Exception("Error: Order not found with id: " + id);
    }
}
