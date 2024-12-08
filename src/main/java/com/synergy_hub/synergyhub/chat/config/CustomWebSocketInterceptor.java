package com.synergy_hub.synergyhub.chat.config;

import com.synergy_hub.synergyhub.auth.exception.JwtAlreadyExpiredException;
import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)  //필터보다 먼저 호출
public class CustomWebSocketInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
            .getAccessor(message, StompHeaderAccessor.class);
        String accessToken = accessor.getFirstNativeHeader("Authorization");

        log.info("===========================================================================");
//        log.info("Received STOMP Message: " + message);
        log.info("Access Token: " + accessToken);
        log.info("Incoming message type: " + accessor.getMessageType());
        log.info("===========================================================================");

        if (accessToken != null) {
            String token = accessToken.substring(BEARER_PREFIX.length()); //Bearer 제거
            if (jwtTokenProvider.isExpired(token)) {
            throw new JwtAlreadyExpiredException(ErrorCode.JWT_ALREADY_EXPIRED);
            }


            Authentication authentication = jwtTokenProvider.createAuthentication(token);
            log.info("====================================================");
            log.info("Authentication: " + authentication);  // 인증 정보 확인

            accessor.setUser(authentication);

//            accessor.getSessionAttributes().put()

        }

        return message;
    }
}
