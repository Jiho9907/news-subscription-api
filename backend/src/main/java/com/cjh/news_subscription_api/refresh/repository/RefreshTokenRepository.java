package com.cjh.news_subscription_api.refresh.repository;

import com.cjh.news_subscription_api.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 리프레시 토큰 정보를 데이터베이스에 저장,조회할 수 있도록 하는 저장소 인터페이스
*/
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
