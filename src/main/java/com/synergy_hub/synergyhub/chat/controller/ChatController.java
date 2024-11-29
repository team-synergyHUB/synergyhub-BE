package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 입장
    @PostMapping("chat/room/enter/{chatRoomId}")
    public ResponseEntity<ChatResponseDto> enterChatRoom(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        ChatResponseDto response = chatService.enterChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok(response);
    }

    // 참여한 채팅 목록 조회
    @GetMapping("chat/members/chats/{memberId}")
    public ResponseEntity<List<ChatResponseDto>> getChats(@PathVariable Long memberId) {
        List<ChatResponseDto> response = chatService.getChats(memberId);
        return ResponseEntity.ok(response);
    }

    // 채팅방 퇴장
    @DeleteMapping("chat/room/exit/{chatRoomId}")
    public ResponseEntity<String> exitChatRoom(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        chatService.exitChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("채팅방에서 퇴장하였습니다.");
    }
}
