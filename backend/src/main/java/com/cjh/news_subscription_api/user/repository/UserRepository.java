package com.cjh.news_subscription_api.user.repository;

import com.cjh.news_subscription_api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 로그인 시 사용
    boolean existsByEmail(String email); // 중복 체크용
    boolean existsByNickname(String nickname); // 닉네임 중복
}
