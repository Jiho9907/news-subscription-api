package com.cjh.news_subscription_api.subscription.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SubscriptionRequestDto {
    private List<String> keywords; // 다중 키워드 처리
//    private String category; // 일단 추가
//    private String source; // 일단 추가
}
