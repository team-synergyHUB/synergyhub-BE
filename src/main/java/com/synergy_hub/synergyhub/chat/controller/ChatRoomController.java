package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestParam Long teamId) {
        ChatRoomResponseDto response = chatRoomService.createChatRoom(teamId);
        return ResponseEntity.ok(response);
    }

    // 모든 채팅방 목록 조회
    @GetMapping("/get")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRooms() {
        List<ChatRoomResponseDto> response = chatRoomService.getChatRooms();
        return ResponseEntity.ok(response);
    }

    // 특정 팀 ID에 해당하는 채팅방 조회
    @GetMapping("/get/{teamId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoomByTeamId(@PathVariable Long teamId) {
        ChatRoomResponseDto response = chatRoomService.getChatRoomByTeamId(teamId);
//        if (response == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat room not found for teamId: " + teamId);
//        }
        return ResponseEntity.ok(response);
    }

    // 채팅방 삭제
    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<Long> deleteChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom deletedRoom = chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok(deletedRoom.getRoomId());
    }
}
