package com.synergy_hub.synergyhub.chat.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatMessageControllerTest {

    @BeforeEach
    void setup() {
        // SecurityContextHolder에 가짜 사용자 설정
        String testEmail = "test@example.com";
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testEmail, null);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testExtractPrincipal() {
        // SecurityContext에서 Principal 추출
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();

        // 결과물 출력
        System.out.println("Extracted Principal Name: " + principalName);

        // Principal이 null이 아니고, 설정된 값과 일치하는지 확인
        assertNotNull(principalName, "Principal should not be null");
        assertEquals("test@example.com", principalName, "Principal name should match the test email");
    }
}
