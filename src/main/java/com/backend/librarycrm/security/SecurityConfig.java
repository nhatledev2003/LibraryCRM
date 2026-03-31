package com.backend.librarycrm.security;

import com.backend.librarycrm.service.LogoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final LogoutService logoutService;

    public SecurityConfig(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì chúng ta dùng JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll() // Cho phép tất cả truy cập đường dẫn này
                        .anyRequest().authenticated() // Các API khác bắt buộc phải có token
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không dùng Session của server
                .logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout") // Endpoint đăng xuất
                .addLogoutHandler(logoutService) // Gọi hàm mình vừa viết
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Trả về JSON thành công khi đăng xuất xong thay vì chuyển trang HTML
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"status\": 200, \"message\": \"Đăng xuất thành công!\"}");
                })
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Mã hóa mật khẩu 1 chiều chuẩn công nghiệp
        return new BCryptPasswordEncoder();
    }
}