package com.synergy_hub.synergyhub.chat.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Collections;
import java.util.Map;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("CustomHandshakeInterceptor beforeHandshake" + request.getURI());

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String authToken = servletRequest.getServletRequest().getHeader("Authorization");
            System.out.println("CustomHandshakeInterceptor beforeHandshake authToken" + authToken);

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7); // "Bearer " 제거
                Authentication authentication = authenticateToken(token); // JWT 검증 로직
                SecurityContextHolder.getContext().setAuthentication(authentication);
                attributes.put("principal", authentication.getPrincipal());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // 핸드셰이크 후 처리 (필요 시)
    }

    private Authentication authenticateToken(String token) {
        // JWT 검증 로직 추가
        // 아래는 예제 코드입니다:
        return new UsernamePasswordAuthenticationToken("user@example.com", null, Collections.emptyList());
    }
}
