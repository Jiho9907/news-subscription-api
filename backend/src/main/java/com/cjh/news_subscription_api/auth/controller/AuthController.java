package com.cjh.news_subscription_api.auth.controller;

import com.cjh.news_subscription_api.auth.dto.*;
import com.cjh.news_subscription_api.auth.service.AuthService;
import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    /**
     * 회원가입 요청 처리
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }
    /**
     * 로그인 요청 처리
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);

        // Refresh Token을 HttpOnly 쿠키에 담아 내려줌
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApiResponse.success(response));
    }

    /**
     * 리프레시 토큰 검증 후 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDto>> refreshAccessToken(
            // 쿠키에서 리프레시 토큰을 꺼내기
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if(refreshToken == null) {
            throw new IllegalArgumentException("RefreshToken 이 없습니다.");
        }

        // 서비스 로직에서 토큰 검증 및 재발급
        RefreshTokenResponseDto response = authService.refreshAccessToken(refreshToken);

        // 새 refreshToken 다시 쿠키로 내려주기
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true) // JS에서 접근 못하게 함
                .secure(true) // HTTPS에서만 동작
                .sameSite("Strict") // CSRF 방지
                .maxAge(Duration.ofDays(14))
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApiResponse.success(response));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal User user) {

        // Redis 토큰 삭제
        authService.logout(user);

        // 쿠키 삭제 위한 Set-Cookie 설정
        ResponseCookie cookie = ResponseCookie.from("refreshToken","")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0) //쿠키 즉시 삭제
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApiResponse.success("로그아웃 되었습니다."));
    }
}
