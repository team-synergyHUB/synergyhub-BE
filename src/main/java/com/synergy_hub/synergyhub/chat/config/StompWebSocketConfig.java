package com.synergy_hub.synergyhub.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Profile("stomp")
@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final CustomHandshakeInterceptor customHandshakeInterceptor;

    public StompWebSocketConfig(CustomHandshakeInterceptor customHandshakeInterceptor) {
        this.customHandshakeInterceptor = customHandshakeInterceptor;
    }

    @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // WebSocket 엔드포인트 등록
            registry.addEndpoint("/ws")
                    .setAllowedOrigins("http://localhost:3000") // CORS 허용
                    .addInterceptors(customHandshakeInterceptor);
//                    .withSockJS(); // SockJS 지원
            registry.addEndpoint("/ws")
                    .setAllowedOrigins("http://localhost:3000") // CORS 허용
                    .addInterceptors(customHandshakeInterceptor)
                    .withSockJS(); // SockJS 지원
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic", "/queue"); // 서버 -> 클라이언트 (topic : 브로드캐스트, queue : 일대일)
            config.setApplicationDestinationPrefixes("/app"); // 클라이언트 -> 서버
            config.setUserDestinationPrefix("/user"); // 특정사용자에게
        }
    }


