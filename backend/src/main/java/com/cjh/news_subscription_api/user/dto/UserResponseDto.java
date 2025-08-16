// 클라이언트에게 사용자 정보를 전달하기 위한 응답 DTO
package com.cjh.news_subscription_api.user.dto;

import com.cjh.news_subscription_api.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String provider;
    private String role;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .role(user.getRole().name())
                .build();
    }
}
