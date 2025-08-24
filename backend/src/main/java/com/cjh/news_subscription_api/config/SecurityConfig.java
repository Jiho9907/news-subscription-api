/**
 * JWT 기반 인증을 사용하는 스프링 시큐리티 설정
 * */
package com.cjh.news_subscription_api.config;

import com.cjh.news_subscription_api.auth.jwt.JwtAuthenticationFilter;
import com.cjh.news_subscription_api.auth.oauth.CustomAuthorizationRequestResolver;
import com.cjh.news_subscription_api.auth.oauth.CustomOAuth2UserService;
import com.cjh.news_subscription_api.auth.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;
    /**
     * 시큐리티 필터와 인증 정책을 정의
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 커스텀 AuthorizationRequestResolver 등록
        OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver =
                new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
        http
                .cors(Customizer.withDefaults()) // 아래 corsConfigurationSource()와 연동
                .csrf(csrf -> csrf.disable()) // CSRF 비활 (CSRF: 웹에서 세션 기반 인증일 때 공격을 막기 위한 보호 기능)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활(JWT는 클라이언트가 토큰을 계속 들고 다니기 때문에 세션 불필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // /api/auth/** → 로그인, 회원가입 등은 인증 없이 접근 가능하게 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증(JWT 토큰)필요
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(customAuthorizationRequestResolver)
                        )
                        .redirectionEndpoint(endpoint ->
                                endpoint.baseUri("/api/auth/google/callback")) // 커스텀 리디렉션 URI 사용
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler) // OAuth2 로그인 성공 시 실행
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

    /**
     * cors설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173")); // 프론트 개발 주소
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // HttpOnly 포함 쿠키 허용
        config.setMaxAge(3600L); // 1시간 캐싱

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 적용
        return source;
    }
}
