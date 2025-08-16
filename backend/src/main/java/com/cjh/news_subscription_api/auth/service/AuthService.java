package com.cjh.news_subscription_api.auth.service;

import com.cjh.news_subscription_api.auth.dto.*;
import com.cjh.news_subscription_api.auth.jwt.JwtUtil;
import com.cjh.news_subscription_api.refresh.service.RefreshTokenService;
import com.cjh.news_subscription_api.user.entity.Role;
import com.cjh.news_subscription_api.user.entity.User;
import com.cjh.news_subscription_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
    * 회원가입 로직
    **/
    @Transactional
    public void signup(SignUpRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if(userRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .nickname(requestDto.getNickname())
                .provider("local")
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    /**
     * 로그인 로직
     **/
    public LoginResponseDto login(LoginRequestDto dto) {
        // 1. 이메일로 사용자 조회(Optional 반환)
        User user = userRepository.findByEmail(dto.getEmail())
                //사용자가 없으면 예외발생
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 입력한 비밀번호(dto.getPassword())와 저장된 비밀번호(user.getPassword()) 비교
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            // 비밀번호 불일치 시 예외 발생
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 이메일을 기반으로 Token 생성
        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        // 4. Redis에 Refresh Token 저장
        refreshTokenService.save(user.getId(), refreshToken);

        // 5. AccessToken 포함 하는 로그인 Dto 생성 후 반환
        return new LoginResponseDto(accessToken, refreshToken);
    }

    /**
     * JWT 기반 리프레시 토큰을 검증, 새로운 액세스 토큰을 발급하는 로직
     */
    @Transactional
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {

        // 1. 토큰 유효성 검사
        if(!jwtUtil.ValidateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        // 2. 토큰에서 이메일 추출(JWT claim 에서 사용자 식별 정보 추출)
        String email = jwtUtil.extractEmail(refreshToken);

        // 3. 사용자 조회(DB 조회)
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 4. Redis의 Refresh Token과 비교
        String storedRefreshToken = refreshTokenService.getToken(user.getId());
        if(!refreshToken.equals(storedRefreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }

        // 5. 새로운 Access, Refresh Token 발급
        String newAccessToken = jwtUtil.createAccessToken(email);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        // 6. Redis에 새 Refresh Token 저장
        refreshTokenService.save(user.getId(), newRefreshToken);

        // 7. 반환
        return new RefreshTokenResponseDto(newAccessToken, newRefreshToken);
    }
}
