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

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // WebSocket 엔드포인트 등록
            registry.addEndpoint("/ws")
                    .setAllowedOrigins("*"); // CORS 허용
//                    .withSockJS(); // SockJS 지원
            registry.addEndpoint("/ws")
                    .setAllowedOrigins("*") // CORS 허용
                    .withSockJS(); // SockJS 지원
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic", "/queue");
            config.setApplicationDestinationPrefixes("/app");
            config.setUserDestinationPrefix("/user");
        }
    }


