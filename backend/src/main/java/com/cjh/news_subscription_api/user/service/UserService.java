// 유저 조회/등록 공통 도우미
package com.cjh.news_subscription_api.user.service;

import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 이메일 기준 사용자 조회
     * @param email
     * @return User 객체(없으면 예외)
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        //이메일로 사용자 조회, 없을 경우 예외 발생
        return userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        //아이디로 사용자 조회, 없을 경우 예외 발생
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다."));
    }
}
