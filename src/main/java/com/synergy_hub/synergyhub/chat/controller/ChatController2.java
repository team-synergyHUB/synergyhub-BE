package com.synergy_hub.synergyhub.chat.controller;

//import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
//import com.synergy_hub.synergyhub.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Controller;

//@RequiredArgsConstructor
//@Controller
//public class ChatController {
//
//    private final ChatService chatService;
//
//    /**
//     * WebSocket 메시지 처리 - websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
//     */
//    @MessageMapping("/chat/message")
//    public void handleChatMessage(ChatMessage chatMessage, @Header("memberId") Long memberId) {
//        chatService.handleMessage(chatMessage, memberId);
//    }
//}
