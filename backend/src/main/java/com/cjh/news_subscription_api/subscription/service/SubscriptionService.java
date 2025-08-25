package com.cjh.news_subscription_api.subscription.service;

import com.cjh.news_subscription_api.subscription.dto.SubscriptionRequestDto;
import com.cjh.news_subscription_api.subscription.entity.Subscription;
import com.cjh.news_subscription_api.subscription.repository.SubscriptionRepository;
import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    public final SubscriptionRepository subscriptionRepository;
    public final UserRepository userRepository;

    /**
     * 사용자의 관심 키워드 등록
     */
    @Transactional
    public void subscribeKeyword(Long userId, SubscriptionRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 구독 키워드 모두 삭제 (전체 초기화 방식)
        List<Subscription> existingSubscriptions = subscriptionRepository.findByUser(user);
        subscriptionRepository.deleteAll(existingSubscriptions);

        // 중복 방지를 위해 Set으로 처리
        Set<String> uniqueKeywords = new HashSet<>(dto.getKeywords());

        List<Subscription> newSubscriptions = uniqueKeywords.stream()
                .map(keyword -> Subscription.builder()
                        .user(user)
                        .keyword(keyword)
//                        .category(dto.getCategory())
//                        .source(dto.getSource())
                        .build())
                .collect(Collectors.toList());

        subscriptionRepository.saveAll(newSubscriptions);
    }

    /**
     * 사용자의 등록된 키워드 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Subscription> getUserSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return subscriptionRepository.findByUser(user);
    }

    /**
     * 사용자의 등록된 키워드 삭제
     */
    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 키워드가 존재하지 않습니다."));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new SecurityException("본인의 키워드만 삭제할 수 있습니다.");
        }

        subscriptionRepository.delete(subscription);
    }
}
