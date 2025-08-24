package com.cjh.news_subscription_api.subscription.entity;

import com.cjh.news_subscription_api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자가 등록한 뉴스 키워드(구독) 정보를 저장하는 엔티티
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    // 유저 정보 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 관심 키워드 (예: "경제", "AI")
    @Column(nullable = false)
    private String keyword;

    // 뉴스 카테고리 (선택, 예: "technology", "business")
    private String category;

    // 뉴스 제공자 (선택, 예: "naver", "newsapi")
    private String source;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
