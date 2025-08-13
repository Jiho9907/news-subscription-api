// 역할(Role)과 권한(Authority)을 한 번에 정의
// 사용자 역할(Role) : USER와 ADMIN
// Spring Security 에서 사용할 권한 정보(authorities)를 연결
package com.cjh.news_subscription_api.user.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;


//Role은 USER, ADMIN 이라는 고정된 값만 가질 수 있는 열거형(enum) 타입
@Getter
public enum Role {

    /**
     * enum 상수 정의
     생성자 인자로 List<SimpleGrantedAuthority>를 받음
     SimpleGrantedAuthority : Spring Security 에서 사용하는 권한 클래스
       - 예: ROLE_USER, ROLE_ADMIN 같은 권한 이름을 감싸는 객체
     Collections.singletonList(...) : 요소 1개만 가진 List를 만드는 메서드
    **/
    USER(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))),
    ADMIN(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

    /**
     각 Role enum 상수는 하나의 authorities 필드를 가짐
     이 필드는 해당 역할에 해당하는 권한 목록을 담고 있음
     예: USER는 "ROLE_USER"라는 권한
    **/
    private final List<SimpleGrantedAuthority> authorities;

    /**
     생성자
      - USER(...), ADMIN(...)에 들어간 인자들이 여기에 전달
      - 받은 authorities를 this.authorities에 저장
    **/
    Role(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
