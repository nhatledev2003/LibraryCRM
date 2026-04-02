package com.backend.librarycrm.security;

import com.backend.librarycrm.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Nếu không có Header Authorization hoặc không bắt đầu bằng "Bearer ", bỏ qua và cho đi tiếp (để vướng vào tường lửa chung)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Cắt lấy chuỗi JWT (bỏ đi 7 ký tự "Bearer ")
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 3. Nếu đọc được username và trong bộ nhớ tạm (SecurityContext) chưa xác thực người này
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. Kiểm tra xem Token này có bị thu hồi trong Database chưa (do đăng xuất hoặc đăng nhập máy khác)
            var isTokenValidInDatabase = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            // 5. Nếu Token vượt qua mọi bài kiểm tra: Hợp lệ + Không hết hạn + Không bị thu hồi
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValidInDatabase) {

                // Cấp thẻ "Đã xác thực" cho Request này
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu vào Context để Spring Security biết người này đã an toàn
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Cho phép Request đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}
