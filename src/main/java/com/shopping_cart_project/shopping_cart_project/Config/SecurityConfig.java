package com.shopping_cart_project.shopping_cart_project.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        //將session設定成無狀態，代表的是，不會讀取後端儲存的session，需要在前端將需要的內容傳入，代表保持登入的功能由前端負責。
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //api開頭的網址需要有權限才能操作，其餘可直接通過
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
                //關閉CSRF防護，我們之後會使用JWT防禦CSRF攻擊。
                .csrf(csrf -> csrf.disable())

        //在基本的filter前，加上JWT認證的filter
                  .addFilterBefore(new JWTAuthenticationFilter(), BasicAuthenticationFilter.class)


        //CORS相關設定
                /*.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        //允許的網址來源
                        config.setAllowedOrigins(Arrays.asList(
                                "http://localhost:5173"
                        ));
                        //允許的HTTP request methods
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                        config.setAllowCredentials(true);
                        //第一次過濾HTTP Header，讓JWT通過
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        //第二次過濾HTTP Header，讓JWT通過
                        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                        //通過後3600秒內，不會再次檢查同樣的request
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))*/;
        return httpSecurity.build();
    }

    //雜湊密碼
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

}
