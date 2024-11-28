package com.synergy_hub.synergyhub.global.exception;

import com.synergy_hub.synergyhub.member.exception.EmailAlreadyExistException;
import com.synergy_hub.synergyhub.member.exception.MemberNotAuthenticatedException;
import com.synergy_hub.synergyhub.member.exception.MemberNotFoundException;
import com.synergy_hub.synergyhub.token.jwt.InvalidPasswordException;
import jakarta.persistence.ElementCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        // 예외 발생 시 로그 기록
        logger.error("CustomException 발생: {}", e.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    // 유효성 검사 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseEntity> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패 시 로그 기록
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        logger.warn("Validation failed: {}", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseEntity.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code("VALIDATION_ERROR") // 적절한 에러 코드 설정
                        .message("Validation failed: " + errorMessage)
                        .build());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponseEntity> handleMemberNotFountException(
        MemberNotFoundException ex) {
        logger.error("MemberNotFountException 발생: {}", ex.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    protected ResponseEntity<ErrorResponseEntity> handleEmailAlreadyExistException(
        EmailAlreadyExistException ex) {
        logger.error("EmailAlreadyExistException 발생: {}", ex.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
    }

    @ExceptionHandler(MemberNotAuthenticatedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleMemberNotAuthenticatedExceptionException(
        MemberNotAuthenticatedException ex) {
        logger.error("MemberNotAuthenticatedException 발생: {}", ex.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected ResponseEntity<ErrorResponseEntity> InvalidPasswordException(
        InvalidPasswordException ex) {
        logger.error("InvalidPasswordException 발생: {}", ex.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
    }

}
