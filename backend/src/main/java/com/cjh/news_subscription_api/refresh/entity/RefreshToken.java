/**
 *  Redis에 저장될 Refresh Token 데이터 구조를 정의
 *  Redis에 "refreshToken:{userId}" 형태의 키로 저장
 */
package com.cjh.news_subscription_api.refresh.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor //모든 필드를 파라미터로 받는 생성자 자동 생성
@RedisHash(
        value = "refreshToken", // Redis 내 저장될 key prefix (namespace)
        timeToLive = 60 * 60 * 24 * 14 // 이 엔티티의 TTL(Time To Live), 14일(초 단위)
)
public class RefreshToken {

    @Id
    private Long userId; // Redis Hash의 키로 사용되는 필드

    private String token; // 실제 저장할 Refresh Token 문자열


}
