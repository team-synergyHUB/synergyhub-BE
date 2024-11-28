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


import com.synergy_hub.synergyhub.chat.dto.ChatMessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatResponseDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatMessageService;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import com.synergy_hub.synergyhub.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 채팅방 입장
    @PostMapping("/rooms/{chatRoomId}/enter")
    public ResponseEntity<ChatResponseDto> enterChatRoom(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        ChatResponseDto response = chatService.enterChatRoom(chatRoomId, memberId).getData();
        return ResponseEntity.ok(response);
    }

    // 참여한 채팅 목록 조회
    @GetMapping("/members/{memberId}/chats")
    public ResponseEntity<List<ChatResponseDto>> getChats(@PathVariable Long memberId) {
        List<ChatResponseDto> response = chatService.getChats(memberId).getData();
        return ResponseEntity.ok(response);
    }

    // 채팅방 퇴장
    @DeleteMapping("/rooms/{chatRoomId}/exit")
    public ResponseEntity<Long> exitChatRoom(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        Long response = chatService.exitChatRoom(chatRoomId, memberId).getData();
        return ResponseEntity.ok(response);
    }

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoomResponseDto response = chatRoomService.createChatRoom(chatRoomRequestDto).getData();
        return ResponseEntity.ok(response);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms() {
        List<ChatRoomResponseDto> response = chatRoomService.getChatRooms().getData();
        return ResponseEntity.ok(response);
    }

    // 채팅방 삭제
    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseEntity<Long> deleteChatRoom(@PathVariable Long chatRoomId) {
        Long response = chatRoomService.deleteChatRoom(chatRoomId).getData();
        return ResponseEntity.ok(response);
    }

    // 메시지 전송
    @PostMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(@PathVariable Long chatRoomId, @RequestBody ChatMessageRequestDto requestDto, @RequestParam Long memberId) {
        ChatMessageResponseDto response = chatMessageService.sendMessage(chatRoomId, requestDto, memberId);
        return ResponseEntity.ok(response);
    }

    // 메시지 조회
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageResponseDto> response = chatMessageService.getChatMessages(chatRoomId).getData();
        return ResponseEntity.ok(response);
    }

    // 메시지 삭제
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Long> deleteMessage(@PathVariable Long messageId, @RequestParam Long memberId) {
        Long response = chatMessageService.deleteMessage(messageId, memberId).getData();
        return ResponseEntity.ok(response);
    }
}
