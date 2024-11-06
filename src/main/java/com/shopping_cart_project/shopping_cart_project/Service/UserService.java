package com.shopping_cart_project.shopping_cart_project.Service;


import com.shopping_cart_project.shopping_cart_project.Config.JWTProvider;
import com.shopping_cart_project.shopping_cart_project.Entity.User;
import com.shopping_cart_project.shopping_cart_project.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

        private UserRepository userRepository;
        private PasswordEncoder passwordEncoder; // 密碼加密器，用於在儲存或驗證密碼時進行加密操作
         private final JWTProvider jwtProvider;
        //建構式

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    //新增用戶
    public void createUser(User user) throws Exception{
        //檢查該email是否已經註冊過
        User isEmailExists = userRepository.findByEmail(user.getEmail());
        //如果該email已經註冊過，就拋出例外，提醒使用者已經註冊
        if (isEmailExists != null){
            throw new Exception("Error: Email is already registered.");
        }
        //如果email 不存在於系統中，則建立新帳戶
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        // 使用密碼加密器加密密碼後再設置
        createUser.setPassword(passwordEncoder.encode((user.getPassword())));
        // 將新用戶資料儲存到資料庫中
        userRepository.save(createUser);
    }
    //依據email找尋用戶
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //用jwt查詢用戶資訊
    public User findUserByJWT(String jwt) throws Exception{
        String email = jwtProvider.getEmailFromJWT(jwt);
        User user = userRepository.findByEmail(email);

        //如果用戶不存在就拋出例外訊息
        if(user == null){
            throw new Exception("Error: Invalid JWT");
        }
        //存在就return
        return user;
    }




}
