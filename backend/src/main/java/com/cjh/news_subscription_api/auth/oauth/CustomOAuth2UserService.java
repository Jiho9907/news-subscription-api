package com.cjh.news_subscription_api.auth.oauth;

import com.cjh.news_subscription_api.user.entity.Role;
import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 구글 로그인 시 사용자 정보를 가져와 DB에 저장 또는 갱신하는 클래스
 * OAuth2 로그인 성공 시 호출
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    /**
     * OAuth2 로그인 성공 시 사용자 정보를 가져와 사용자 객체 반환
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글에서 받은 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.get("name");

        // 이미 존재하는 유저인지 확인
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user = existingUser.orElseGet(() -> {
            // 없으면 새 유저 생성 (소셜 로그인은 비밀번호 없음)
            return userRepository.save(User.builder()
                    .email(email)
                    .nickname(nickname)
                    .password("NO_PASSWORD")
                    .provider("GOOGLE")
                    .role(Role.USER)
                    .build());
        });

        return new DefaultOAuth2User(
                user.getAuthorities(),
                attributes,
                "sub" // 구글에서 고유 식별자 (보통 "sub")를 사용
        );
    }
}
