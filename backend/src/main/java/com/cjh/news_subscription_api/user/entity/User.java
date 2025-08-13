package com.cjh.news_subscription_api.user.entity;
/**
 * DB의 users 테이블과 1:1 매핑
 * 사용자 정보(이메일, 비밀번호, 닉네임 등)를 담는 객체
 * Spring Security의 UserDetails를 구현해서 로그인/인증에 사용
 * */
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//기본 생성자 자동 생성 (User()), protected 접근제한자 적용
@AllArgsConstructor //모든 필드 받는 생성자 생성
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String provider; //일반회원(LOCAL), 소셜로그인(GOOGLE 등)

    @Enumerated(EnumType.STRING) // DB에 USER, ADMIN 이라는 문자열로 저장
    @Column(nullable = false)
    private Role role; // USER, ADMIN

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist // JPA 에서 엔티티가 저장되기 직전에 자동 호출되는 메서드
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /** implements UserDetails
     - 이 클래스가 Spring Security의 로그인 시스템에서 사용자 정보로 사용
     - UserDetails는 로그인 사용자에 대한 정보를 정의한 인터페이스
    **/
    // 사용자가 가진 권한 목록을 반환(ROLE_USER, ROLE_ADMIN 같은 권한 리스트)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
    // 로그인 시 사용자 식별자로 사용될 값
    // Spring Security에서는 이 username을 ID처럼 사용
    // 이메일을 로그인 ID로 사용할 것임
    @Override
    public String getUsername() {
        return email; // 로그인 시 식별자
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 활성화 여부
    }
}
