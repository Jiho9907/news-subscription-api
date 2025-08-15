// JWT 토큰 생성, 파싱, 유효성 검사 등의 핵심 기능을 담당
package com.cjh.news_subscription_api.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; //JWT 비밀키나 만료시간 같은 설정값을 외부 설정에서 받아오려면 반드시 스프링 @Value 사용
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // 스프링 빈으로 등록(의존성 주입)
@RequiredArgsConstructor // final 또는 @NonNull 필드에 대한 생성자 생성
public class JwtUtil {

    // application.yml 에서 JWT 관련 설정 주입 받음
    @Value("${jwt.secret}") // 비밀키 (Base64 인코딩된 문자열)
    private String secretKey;

    @Value("${jwt.access-token-expiration}") // 액세스 토큰 만료 시간
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}") // 리프레시 토큰 만료 시간
    private long refreshTokenExpiration;

    private Key key; // JWT 서명을 위한 비밀 키 객체

    @PostConstruct
    public void init(){
        // secretKey를 바이트 배열로 변환하여 HMAC-SHA 알고리즘에 맞는 Key 객체 생성
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 사용자의 이메일 기반 액세스 토큰 생성
    public String createAccessToken(String email) {
        return createToken(email, accessTokenExpiration);
    }

    // 사용자 이메일 기반 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenExpiration);
    }

    // 공통 JWT 생성 로직
    private String createToken(String email, long expirationMillis) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email) //토큰 주제(사용자 식별자)
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(new Date(now.getTime() + expirationMillis)) // 토큰 만료시간
                .signWith(key, SignatureAlgorithm.HS256) // 비밀 키로 서명 (HMAC-SHA256 사용)
                .compact(); // JWT 문자열 생성
    }

    // 토큰에서 이메일(subject)을 추출
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰 유효성 검사(만료여부, 서명 유효성 등)
    public boolean ValidateToken(String token) {
        try {
            getClaims(token); // 예외 발생 안 하면 유효한 토큰
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false; // 유효하지 않은 토큰 (만료, 변조 등)
        }
    }

    // 토큰에서 Claims 객체를 파싱 (이메일, 만료시간 등 포함)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 검증에 사용될 키
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 서명 검증
                .getBody(); // Claims 반환
    }
}
