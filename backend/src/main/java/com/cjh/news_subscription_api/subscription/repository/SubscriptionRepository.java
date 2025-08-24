package com.cjh.news_subscription_api.subscription.repository;

import com.cjh.news_subscription_api.subscription.entity.Subscription;
import com.cjh.news_subscription_api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // 특정 유저의 모든 구독 키워드 조회
    List<Subscription> findByUser(User user);

    // 유저 + 키워드 조합으로 중복 방지
    boolean existsByUserAndKeyword(User user, String keyword);

    List<Subscription> findByUserId(Long userId);
}
