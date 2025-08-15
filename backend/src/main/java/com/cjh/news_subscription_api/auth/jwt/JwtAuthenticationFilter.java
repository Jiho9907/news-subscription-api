package com.cjh.news_subscription_api.auth.jwt;

import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 사용자의 요청에서 JWT를 추출해 검증
 * → 인증 객체 등록 (유효하면 해당 사용자의 인증 정보를 Spring Security의 SecurityContext에 저장)
 * 이 작업을 자동으로 처리해주는 커스텀필터
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        // 2. 토큰이 없거나 Bearer로 시작하지 않으면 다음 필터로 넘김
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. JWT 추출
        String token = authHeader.substring(7); //"Bearer " 이후 토큰

        // 4. 토큰 유효성 검증
        if(!jwtUtil.ValidateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 이메일 추출 및 사용자 조회
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user, // principal(UserDetails)
                            null,
                            user.getAuthorities() // ROLE 목록
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // 7. SecurityContext에 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 8. 다음 필터로 전달
        filterChain.doFilter(request, response);

    }
}
