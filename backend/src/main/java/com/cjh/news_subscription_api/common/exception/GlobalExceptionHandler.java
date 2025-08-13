package com.cjh.news_subscription_api.common.exception;

import com.cjh.news_subscription_api.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 전역 처리
public class GlobalExceptionHandler {

    // MethodArgumentNotValidException이 발생하면 handleValidationException() 메서드가 실행
    // 이 예외는 DTO 유효성 검사 실패 (@Valid) 시에 자동으로 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 유효성 검사에 실패한 필드 중 첫 번째 에러 메시지
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        // HTTP 상태 코드 400 (Bad Request)을 응답
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorMessage));
    }
}
