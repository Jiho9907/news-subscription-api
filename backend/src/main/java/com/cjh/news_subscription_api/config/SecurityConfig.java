/**
 * JWT 기반 인증을 사용하는 스프링 시큐리티 설정
 * */
package com.cjh.news_subscription_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 비밀번호 암호화를 위한 Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // (스프링 시큐리티 제공) 비밀번호를 암호화(해싱)하는 클래스 - BCrypt 해시 함수를 이용
    }
}
