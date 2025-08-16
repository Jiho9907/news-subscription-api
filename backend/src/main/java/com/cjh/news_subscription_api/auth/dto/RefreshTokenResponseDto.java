package com.cjh.news_subscription_api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
