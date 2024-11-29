package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoomResponseDto response = chatRoomService.createChatRoom(chatRoomRequestDto);
        return ResponseEntity.ok(response);
    }

    // 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms() {
        List<ChatRoomResponseDto> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }
}
