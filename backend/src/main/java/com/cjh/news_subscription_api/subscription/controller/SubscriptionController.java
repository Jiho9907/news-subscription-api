package com.cjh.news_subscription_api.subscription.controller;

import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.subscription.dto.SubscriptionRequestDto;
import com.cjh.news_subscription_api.subscription.entity.Subscription;
import com.cjh.news_subscription_api.subscription.service.SubscriptionService;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    // 키워드 등록
    @PostMapping
    public ApiResponse<String> subscribeKeyword(
            @AuthenticationPrincipal User user,
            @RequestBody SubscriptionRequestDto dto) {
        subscriptionService.subscribeKeyword(user.getId(), dto);
        return ApiResponse.success("키워드 등록 성공");
    }

    // 키워드 목록 조회
    @GetMapping
    public ApiResponse<List<Subscription>> getUserSubscription(
            @AuthenticationPrincipal User user) {
        List<Subscription> list = subscriptionService.getUserSubscriptions(user.getId());
        return ApiResponse.success(list);
    }

    // 키워드 삭제
    @DeleteMapping("/{subscriptionId}")
    public ApiResponse<String> deleteKeyword(
            @AuthenticationPrincipal User user,
            @PathVariable Long subscriptionId) {

        subscriptionService.deleteSubscription(user.getId(), subscriptionId);
        return ApiResponse.success("키워드 삭제 완료");
    }
}
