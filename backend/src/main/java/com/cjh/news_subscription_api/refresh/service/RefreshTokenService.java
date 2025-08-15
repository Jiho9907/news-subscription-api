package com.cjh.news_subscription_api.refresh.service;

import com.cjh.news_subscription_api.refresh.entity.RefreshToken;
import com.cjh.news_subscription_api.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 필드를 자동으로 생성자 주입
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 리프레시 토큰 저장 메서드
     * @param userId 사용자 ID
     * @param token  새로 발급된 리프레시 토큰
     */
    public void save(Long userId, String token) {
        // RefreshToken 객체 생성(userId와 token 포함)
        RefreshToken refreshToken = new RefreshToken(userId, token);
        // DB 저장
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 특정 사용자 ID 해당하는 리프레시 토큰 조회 메서드
     * @param userId 사용자 ID
     * @return 해당 사용자의 리프레시 토큰 (없으면 null 반환)
     */
    public String getToken(Long userId) {
        // ID(pk) 기준으로 토큰 조회, 존재하면 토큰 문자열 반환
        return refreshTokenRepository.findById(userId)
                .map(RefreshToken::getToken) // 존재하면 getToken() 호출
                .orElse(null); // 없으면 null 반환
    }
}
