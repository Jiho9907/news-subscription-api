// JWT 관련 설정값을 담는 설정 클래스
package com.cjh.news_subscription_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
// application.yml 에서 "jwt"로 시작하는 프로퍼티들을 이 클래스 필드에 바인딩
public class JwtConfig {

    // JWT 토큰을 생성하고 검증할 때 사용할 비밀 키(jwt.secret에 매핑)
    private String secret;

    // 액세스 토큰(access token)의 만료 시간 (jwt.accessTokenExpiration에 매핑)
    private long accessTokenExpiration;

    // 리프레시 토큰(refresh token)의 만료 시간 (jwt.refreshTokenExpiration에 매핑)
    private long refreshTokenExpiration;
}
