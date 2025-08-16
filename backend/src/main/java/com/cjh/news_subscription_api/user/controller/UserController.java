package com.cjh.news_subscription_api.user.controller;

import com.cjh.news_subscription_api.common.response.ApiResponse;
import com.cjh.news_subscription_api.user.dto.UserResponseDto;
import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * 현재 로그인한 사용자 정보 반환
     */
    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getMyInfo(@AuthenticationPrincipal User user) {
        // @AuthenticationPrincipal : 스프링 시큐리티가 현재 로그인한 인증된 User 객체를 주입 받음

        // 최신 사용자 정보 조회  (세션, 캐시가 아닌 DB 기준)
        User fullUser = userService.findById(user.getId());

        // User 엔티티를 UserResponseDto로 변환 후 ApiResponse.success() 래퍼로 감싸 반환
        return ApiResponse.success(UserResponseDto.from(fullUser));
    }
}
