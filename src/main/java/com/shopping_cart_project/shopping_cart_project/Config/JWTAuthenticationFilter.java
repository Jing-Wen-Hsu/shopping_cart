package com.shopping_cart_project.shopping_cart_project.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

//1. 類別定義  //繼承自 OncePerRequestFilter，確保在同一請求中只執行一次過濾邏輯，避免重複執行
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    //2. 過濾邏輯
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //3. 取得 JWT   //取得header中Authorization中的內容，裡面有JWT
        String jwt = request.getHeader("Authorization");
//4. 檢查 JWT 是否存在
        if(jwt != null){
            //5. 處理 Bearer Token
            //JWT 通常以 "Bearer " 開頭，所以這裡去掉前面的 "Bearer " 字串，只留下實際的 JWT 部分，重新賦值給jwt
            //因為substring是從0開始，所以-1。
            jwt = jwt.substring("Bearer ".length() - 1);
            //6. 驗證 JWT
            //有效就可以提供授權給這個用戶，無效或過期的JWT會發生例外，會跳到catch部分。
            try{
                SecretKey key = Keys.hmacShaKeyFor(JWTConstant.SECRET != null ? JWTConstant.SECRET.getBytes() : null);
                //取得JWT中的payload區塊內容
                Claims claims = Jwts.parserBuilder()//建立JWT解讀器
                        .setSigningKey(key)//設定檢驗的密鑰
                        .build()//產生解讀器
                        .parseClaimsJws(jwt)//解讀JWT
                        .getBody();//取得payload內容
                //7. 提取使用者信息  //從JWT payload取得email
                String email = String.valueOf(claims.get("email"));
                //8. 設定認證
                //有效的JWT就等於本人輸入正確的email和密碼，可以通過驗證
                //授權中帶有用戶的email，代表授權是只給這個用戶使用。
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
                //設定通過Spring Security的認證
                //將這個認證物件設定到 Spring Security 的上下文中，使後續的請求可以使用這個認證
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e){
                //9. 例外處理  //驗證時發現JWT無效或過期，就顯示錯誤
                //JWT無效或過期，就顯示錯誤
                throw new BadCredentialsException("Invalid JWT or expired");
            }
        }
        //  10. 傳遞請求 //把request傳給下個filter使用
        filterChain.doFilter(request, response);
    }
}
