package com.synergy_hub.synergyhub.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Profile("stomp")
@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final CustomHandshakeInterceptor customHandshakeInterceptor;
    private final CustomWebSocketInterceptor customWebSocketInterceptor;


    //    public StompWebSocketConfig(CustomHandshakeInterceptor customHandshakeInterceptor) {
//        this.customHandshakeInterceptor = customHandshakeInterceptor;
//    }

    @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // WebSocket 엔드포인트 등록
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS();
//            .setInterceptors(customHandshakeInterceptor);

////                    .withSockJS(); // SockJS 지원
//            registry.addEndpoint("/ws")
////                    .setAllowedOrigins("*") // CORS 허용
//                .setAllowedOriginPatterns("*")
//                    .withSockJS(); // SockJS 지원
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic", "/queue"); // 서버 -> 클라이언트 (topic : 브로드캐스트, queue : 일대일)
            config.setApplicationDestinationPrefixes("/app"); // 클라이언트 -> 서버
            config.setUserDestinationPrefix("/user"); // 특정사용자에게
        }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customWebSocketInterceptor);
    }
    }


