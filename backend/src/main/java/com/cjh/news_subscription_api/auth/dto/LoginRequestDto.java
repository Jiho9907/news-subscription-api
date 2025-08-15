package com.cjh.news_subscription_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
