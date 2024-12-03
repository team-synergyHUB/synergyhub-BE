package com.synergy_hub.synergyhub.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T payload, HttpStatus status) {
        return ResponseEntity
            .status(status)
            .body(ApiResponse.<T>builder()
                .message(message)
                .payload(payload)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(String message, HttpStatus status) {
        return ResponseEntity
            .status(status)
            .body(ApiResponse.<T>builder()
                .message(message)
                .payload(null)
                .build());
    }



}
