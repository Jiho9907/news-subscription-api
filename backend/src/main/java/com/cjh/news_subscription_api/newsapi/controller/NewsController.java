package com.cjh.news_subscription_api.newsapi.controller;

import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.newsapi.service.NewsApiService;
import com.cjh.news_subscription_api.subscription.repository.SubscriptionRepository;
import com.cjh.news_subscription_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsApiService newsApiService;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping("/recommend")
    public ApiResponse<Map<String, List<Map<String, String>>>> getRecommendedNews(
            @AuthenticationPrincipal User user) {
        Map<String, List<Map<String, String>>> result = new HashMap<>();

        subscriptionRepository.findByUserId(user.getId())
                .forEach(subscription -> {
                    String keyword = subscription.getKeyword();
                    List<Map<String, String>> news = newsApiService.searchNews(keyword);
                    result.put(keyword, news);
                });

        return ApiResponse.success(result);
    }
}
