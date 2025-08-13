package com.cjh.news_subscription_api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }
}
