//package com.synergy_hub.synergyhub.chat.service;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//public class ChatRoomSessionManager {
//
//    private final Map<Long, Set<WebSocketSession>> sessionMap = new HashMap<>();
//
//    public void addSession(Long chatRoomId, WebSocketSession session) {
//        sessionMap.computeIfAbsent(chatRoomId, k -> new HashSet<>()).add(session);
//    }
//
//    public void removeSession(Long chatRoomId, WebSocketSession session) {
//        Set<WebSocketSession> sessions = sessionMap.get(chatRoomId);
//        if (sessions != null) {
//            sessions.remove(session);
//            if (sessions.isEmpty()) {
//                sessionMap.remove(chatRoomId);
//            }
//        }
//    }
//
//    public Set<WebSocketSession> getSessions(Long chatRoomId) {
//        return sessionMap.getOrDefault(chatRoomId, new HashSet<>());
//    }
//}
