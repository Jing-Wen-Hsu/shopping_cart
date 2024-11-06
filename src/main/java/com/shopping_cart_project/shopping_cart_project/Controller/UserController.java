package com.shopping_cart_project.shopping_cart_project.Controller;

import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService; //引入UserService
    //建構式
    public UserController(UserService userService) {
        this.userService = userService;
    }
    //取得目前登入的用戶資訊
    @GetMapping("/")
        //接受一個名為 Authorization 的 HTTP 標頭，並將其值賦給 jwt 變數。這裡用來獲取用戶的 JWT。
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String jwt) throws Exception{
        User user =userService.findUserByJWT(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
