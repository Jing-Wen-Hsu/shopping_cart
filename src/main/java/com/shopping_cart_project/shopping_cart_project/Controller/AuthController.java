package com.shopping_cart_project.shopping_cart_project.Controller;

import com.fasterxml.jackson.core.JsonPointer;
import com.shopping_cart_project.shopping_cart_project.Config.JWTProvider;

import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Response.AuthResponse;
import com.shopping_cart_project.shopping_cart_project.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")  //定義請求路徑
public class AuthController {
    private final JWTProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    //建構式
    public AuthController(JWTProvider jwtProvider, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //註冊
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        //處理用戶註冊
        userService.createUser(user);

        //使用JWTProvider產生token，在註冊時，要傳送email給JWTProvider
        //夾帶 email 是為了在不查資料庫的情況下，讓服務端快速辨識用戶身份。
        String token = jwtProvider.generateToken(user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    //登入
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody User user) throws Exception {
        //確認登入的帳號是否存在，根據提供的email在資料庫中找尋用戶
        User foundUser = userService.findUserByEmail(user.getEmail());

        //輸入的帳號有誤、不存在或密碼錯誤，拋出例外    //比對資料庫中該用戶的密碼以及用戶輸入的密碼
        //passwordEncoder要引入並且給建構式
        if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new Exception("帳號或密碼有誤"); //不明確指出哪個有誤，避免有心人取得有效的email
        }
        //沒問題，產生token
        String token = jwtProvider.generateToken(foundUser.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("登入成功");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}

