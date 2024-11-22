//package com.synergy_hub.synergyhub.chat.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//
//    private final ChatRoomService chatRoomService;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public ChatWebSocketHandler(ChatRoomService chatRoomService, ObjectMapper objectMapper) {
//        this.chatRoomService = chatRoomService;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        // 메시지 처리 로직 추가
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        // 세션 삭제 로직 추가
//    }
//}
//
