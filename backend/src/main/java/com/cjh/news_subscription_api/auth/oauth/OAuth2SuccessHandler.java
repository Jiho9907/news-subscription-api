package com.cjh.news_subscription_api.auth.oauth;

import com.cjh.news_subscription_api.auth.jwt.JwtUtil;
import com.cjh.news_subscription_api.refresh.service.RefreshTokenService;
import com.cjh.news_subscription_api.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 시 호출되는 핸들러
 * JWT 및 리프레시 토큰 발급 + 쿠키 반환
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();

        // 1. JWT 발급
        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        // 2. 리프레시 토큰 DB 저장
        refreshTokenService.save(user.getId(), refreshToken);

        // 3. 리프레시 토큰을 HttpOnly 쿠키로 클라이언트에 전달
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // HTTPS 환경이라면 반드시 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        response.addCookie(refreshCookie);

        // 4. accessToken을 URL로 넘겨서 리디렉트
        String redirectUrl = "http://localhost:5173/oauth2/redirect?accessToken=" + accessToken;
        response.sendRedirect(redirectUrl);;
    }
}
