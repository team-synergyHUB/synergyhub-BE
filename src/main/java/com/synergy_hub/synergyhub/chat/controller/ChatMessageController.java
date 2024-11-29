package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 메시지 전송
    @PostMapping("/rooms/{chatRoomId}")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(@PathVariable Long chatRoomId, @RequestBody ChatMessageRequestDto requestDto, @RequestParam Long memberId) {
        ChatMessageResponseDto response = chatMessageService.sendMessage(chatRoomId, requestDto, memberId);
        return ResponseEntity.ok(response);
    }

    // 메시지 조회
    @GetMapping("/rooms/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageResponseDto> response = chatMessageService.getChatMessages(chatRoomId);
        return ResponseEntity.ok(response);
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId, @RequestParam Long memberId) {
        chatMessageService.deleteMessage(messageId, memberId);
        return ResponseEntity.ok("메시지가 삭제되었습니다.");
    }
}
