package com.cjh.news_subscription_api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    // 로그인 성공 시 클라이언트에게 발급해줄 JWT 인증 토큰을 담는 필드
    private String accessToken;
}
