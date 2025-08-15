package com.cjh.news_subscription_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /**
     * RedisConnectionFactory
     * - Redis와 연결하는 커넥션 팩토리
     * - Lettuce 사용 : 비동기 논블로킹 Redis 클라이언트
     * - 기본값 localhost:6379로 연결
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * RedisTemplate 등록
     * - Redis에 데이터를 저장하고 조회하기 위한 핵심 객체
     * - key와 value의 직렬화 방식을 지정
     * > StringRedisSerializer → 문자열만 저장/조회
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory());

        // (key, value) 직렬화 방식 설정
        template.setKeySerializer(new StringRedisSerializer()); // key: String
        template.setValueSerializer(new StringRedisSerializer()); // value: String (필요 시 JsonSerializer로 변경)

        return template;
    }
}
