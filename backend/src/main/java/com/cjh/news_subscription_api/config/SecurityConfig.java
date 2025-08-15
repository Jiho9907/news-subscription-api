/**
 * JWT 기반 인증을 사용하는 스프링 시큐리티 설정
 * */
package com.cjh.news_subscription_api.config;

import com.cjh.news_subscription_api.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 시큐리티 필터와 인증 정책을 정의
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활 (CSRF: 웹에서 세션 기반 인증일 때 공격을 막기 위한 보호 기능)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활(JWT는 클라이언트가 토큰을 계속 들고 다니기 때문에 세션 불필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // /api/auth/** → 로그인, 회원가입 등은 인증 없이 접근 가능하게 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증(JWT 토큰)필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //사용자의 JWT 토큰을 검사하는 커스텀 필터(기존 로그인 필터보다 먼저 토큰을 체크)
        return http.build();
    }

    /**
     * 비밀번호 암호화를 위한 Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // (스프링 시큐리티 제공) 비밀번호를 암호화(해싱)하는 클래스 - BCrypt 해시 함수를 이용
    }

    /**
     * 인증 매니저 설정
     * 로그인 시 사용자 인증을 수행할 때 필요한 AuthenticationManager를 Bean으로 등록
     * 예: 로그인 서비스에서 authenticationManager.authenticate(...) 식으로 사용 가능
     * @param config
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
