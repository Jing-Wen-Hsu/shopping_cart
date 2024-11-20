package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Cart;
import com.shopping_cart_project.shopping_cart_project.Entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Repository.CartRepository;
import com.shopping_cart_project.shopping_cart_project.Request.AddItemRequest;
import org.springframework.stereotype.Service;

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
            CartItem createCartItem = cartItemService.createCartIitem(cartItem);
            //將此新增的 CartItem 加入購物車的 cartItems 清單中。
            cart.getCartItems().add(createCartItem);
            //調用 calcCartTotal 方法重新計算購物車總金額。
            calcCartTotal(userId);
        }
    }
}
