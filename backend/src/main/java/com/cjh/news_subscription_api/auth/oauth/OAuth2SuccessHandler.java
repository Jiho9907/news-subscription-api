package com.cjh.news_subscription_api.auth.oauth;

import com.cjh.news_subscription_api.auth.jwt.JwtUtil;
import com.cjh.news_subscription_api.refresh.service.RefreshTokenService;
import com.cjh.news_subscription_api.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 로그인 성공 시 호출되는 핸들러
 * JWT 및 리프레시 토큰 발급 + 쿠키 반환
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        // JWT 생성
        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken(email);

        // Refresh 토큰 저장
        User user = (User) userDetails;
        refreshTokenService.save(user.getId(), refreshToken);

        // Refresh Token은 HttpOnly 쿠키로 저장
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일

        response.addCookie(refreshCookie);

        // 액세스 토큰은 JSON으로 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("accessToken", accessToken);
        objectMapper.writeValue(response.getWriter(), tokenResponse);
    }
}
