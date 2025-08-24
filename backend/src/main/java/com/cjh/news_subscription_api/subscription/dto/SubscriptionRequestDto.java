package com.cjh.news_subscription_api.subscription.dto;

import lombok.Getter;

@Getter
public class SubscriptionRequestDto {
    private String keyword;
    private String category; // 일단 추가해둠
    private String source;   // 일단 추가해둠
}
