package com.synergy_hub.synergyhub.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatRoomSessionManager sessionManager, ObjectMapper objectMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
    }

    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto) {
        ChatRoom chatRoom = new ChatRoom(
                null,
                dto.getTeamId(),
                dto.getRoomName(),
                dto.getRoomState(),
                null,
                false
        );
        chatRoom = chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(
                chatRoom.getRoomId(),
                chatRoom.getRoomName(),
                chatRoom.getRoomState(),
                chatRoom.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    public List<ChatRoomResponseDto> getChatRoomsByTeamId(Long teamId, int page, int size) {
        return chatRoomRepository.findByTeamIdAndIsDeletedFalse(teamId)
                .stream()
                .map(chatRoom -> new ChatRoomResponseDto(
                        chatRoom.getRoomId(),
                        chatRoom.getRoomName(),
                        chatRoom.getRoomState(),
                        chatRoom.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                ))
                .collect(Collectors.toList());
    }

    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));
        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }

    public void addSession(Long chatRoomId, WebSocketSession session) {
        sessionManager.addSession(chatRoomId, session);
    }

    public void removeSession(Long chatRoomId, WebSocketSession session) {
        sessionManager.removeSession(chatRoomId, session);
    }

    public void sendMessage(Long chatRoomId, Object message) {
        Set<WebSocketSession> sessions = sessionManager.getSessions(chatRoomId);

        if (sessions.isEmpty()) {
            throw new IllegalStateException("No active sessions found for chatRoomId: " + chatRoomId);
        }

        try {
            String payload = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(payload);

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) { // 세션이 열려 있는 경우에만 전송
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        // 특정 세션에서 메시지 전송 실패 처리
                        System.err.println("Failed to send message to session: " + session.getId());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Session is closed: " + session.getId());
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

}
