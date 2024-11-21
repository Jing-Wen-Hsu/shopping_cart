package com.shopping_cart_project.shopping_cart_project.Service;

import com.shopping_cart_project.shopping_cart_project.Entity.Cart;
import com.shopping_cart_project.shopping_cart_project.Entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.Entity.Product;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Repository.CartItemRepository;
import com.shopping_cart_project.shopping_cart_project.Repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    //檢查商品是否在購物車中
    public CartItem isCartItemInCart(Cart cart, Product product) {
        return cartItemRepository.isCartItemInCart(cart);
    }

    //加入購物車 ->創建購物車商品
        //如果數量小於0，將數量設為1，以及計算價格。
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(Math.max(cartItem.getQuantity(),1));
        cartItem.setPrice((cartItem.getProduct().getPrice() * cartItem.getQuantity()));
        return cartItemRepository.save(cartItem);
    }

    //更新購物車商品
        //重新計算數量和價格，並儲存到資料庫
    public CartItem updateCartItem (Long userId , Long id , CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getCart().getUser().getId());

        //更新前會確認發送請求的用戶和購物車的擁有者是否相同，避免修改到別人的購物車。
        if(user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        }
        return cartItemRepository.save(item);
    }
}
