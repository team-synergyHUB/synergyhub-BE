//package com.synergy_hub.synergyhub.chat.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // WebSocket 요청에서 CSRF 비활성화
////                .authorizeHttpRequests()
////                .requestMatchers("/ws-stomp/**").permitAll() // WebSocket 엔드포인트 허용
//                .anyRequest().permitAll(); // 나머지 요청은 인증 필요
//        return http.build();
//    }
//}
