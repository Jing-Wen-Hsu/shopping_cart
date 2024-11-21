package com.shopping_cart_project.shopping_cart_project.Controller;

import com.shopping_cart_project.shopping_cart_project.Entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Service.CartItemService;
import com.shopping_cart_project.shopping_cart_project.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {
    private final UserService userService;
    private final CartItemService cartItemService;

    public CartItemController(UserService userService, CartItemService cartItemService) {
        this.userService = userService;
        this.cartItemService = cartItemService;
    }

    //修改商品數量
    @PutMapping("/{cartItemId}")
    public ResponseEntity<String> updateCartItem (@PathVariable("cartItemId") Long id,
                                                  @RequestBody CartItem cartItem,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        cartItemService.updateCartItem(user.getId(), id, cartItem);

        return new ResponseEntity<>("Cart item updated successfully", HttpStatus.OK);
    }


    //刪除商品
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem (@PathVariable("cartItemId") Long id,
                                                  @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJWT(jwt);
        cartItemService.removeCartItem(user.getId(),id);
        return new ResponseEntity<>("CartItem deleted successfully", HttpStatus.OK);
    }

}
