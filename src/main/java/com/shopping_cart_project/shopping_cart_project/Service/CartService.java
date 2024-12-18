package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Cart;
import com.shopping_cart_project.shopping_cart_project.Entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Repository.CartRepository;
import com.shopping_cart_project.shopping_cart_project.Request.AddItemRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private  final CartItemService cartItemService;

    public CartService(CartRepository cartRepository, ProductService productService , CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    //建立購物車
    public Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user); //關聯用戶
        return cartRepository.save(cart);
    }

    //加入購物車
        //檢查購物車內是否有相同的商品，沒有的話創建一個新的cartItem後將商品加入購物車。
        //req：封裝新增商品請求的物件，包含 productId（商品ID）及 quantity（購買數量）。
    public void addToCart(Long userId, AddItemRequest req) throws Exception{
        //先找出用戶的購物車以及要新增的商品，才能檢查購物車內是否已存在相同商品
        //從資料庫中查詢該用戶的購物車
        Cart cart = cartRepository.findCartByUserId(userId);
        //根據請求中的productId 從商品服務中查詢商品詳細資料（product）。
        Product product = productService.getProductById(req.getProductId());

        //檢查要新增的商品是否已存在購物車中
        CartItem isPresent = cartItemService.isCartItemInCart(cart,product);
        if(isPresent == null) { //購物車內無相同商品
            //創建一個新的cartItem空間存放
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart); //跟購物車關聯
            cartItem.setProduct(product); //跟商品關聯
            cartItem.setQuantity(req.getQuantity()); //set購買數量
            cartItem.setPrice(req.getQuantity() * product.getPrice()); //單一商品金額小計
            //將商品加入購物車
            CartItem createCartItem = cartItemService.createCartItem(cartItem);
            //將此新增的 CartItem 加入購物車的 cartItems 清單中。
            cart.getCartItems().add(createCartItem);
            //調用 calcCartTotal 方法重新計算購物車總金額。
            calcCartTotal(userId);
        }
    }

    //計算購物車內的商品數量和價格
    public Cart calcCartTotal(Long userId){
        //獲取用戶購物車
        Cart cart = cartRepository.findCartByUserId(userId);
        //totalPrice、totalQuantity用於儲存購物車內所有商品的總金額及總數量。
            //初始的商品數量及價格皆預設為0。
        int totalPrice = 0 ;
        int totalQuantity = 0 ;

        for (CartItem cartItem : cart.getCartItems()){
            totalPrice += cartItem.getPrice();
            totalQuantity += cartItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);
        return cartRepository.save(cart);
    }

    //清除購物車 ->清空購物車中的商品，重新設定購物車的總價格、總商品數為0，並返回商品總額。
    public Integer clearCart (Long userId) throws Exception{
        //找出用戶的購物車
        Cart cart = cartRepository.findCartByUserId(userId);
        Integer totalPrice = cart.getTotalPrice(); //取得當前用戶購物車的總額
        //使用 Iterator 迭代器，遍歷購物車中的商品，進行移除
        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {  //hasNext() 檢查集合中是否還有下一個元素
            CartItem cartItem = iterator.next(); //next() 返回集合中的下一個元素。
            cartItemService.removeCartItem(userId,cartItem.getId()); //從資料庫中刪除。
            iterator.remove(); //從集合移除
        }
        cart.setTotalPrice(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);

        return totalPrice;
    }
}
