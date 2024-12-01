package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/chat/room/create")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestParam Long teamId) {
        ChatRoomResponseDto response = chatRoomService.createChatRoom(teamId);
        return ResponseEntity.ok(response);
    }

    // 채팅방 목록 조회
    @GetMapping("/chat/room/get")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms() {
        List<ChatRoomResponseDto> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 채팅방 삭제
    @DeleteMapping("/chat/room/delete/{chatRoomId}")
    public ResponseEntity<Long> deleteChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom deletedRoomId =  chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok(deletedRoomId.getRoomId());
    }
}
