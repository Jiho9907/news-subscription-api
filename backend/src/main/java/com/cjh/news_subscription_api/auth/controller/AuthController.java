package com.cjh.news_subscription_api.auth.controller;

import com.cjh.news_subscription_api.auth.dto.LoginRequestDto;
import com.cjh.news_subscription_api.auth.dto.LoginResponseDto;
import com.cjh.news_subscription_api.auth.dto.SignUpRequestDto;
import com.cjh.news_subscription_api.auth.service.AuthService;
import com.cjh.news_subscription_api.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
