package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    // 메시지 전송
    @MessageMapping ("/message/send/{chatRoomId}")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageRequestDto requestDto,
            @Header("userId") Principal principal) {
        Long memberId = Long.valueOf(principal.getName()); // Principal에서 memberId를 가져온다고 가정
        ChatMessageResponseDto response = chatMessageService.sendMessage(chatRoomId, requestDto, memberId);
        return ResponseEntity.ok(response);
    }

    // 메시지 조회
    @MessageMapping ("/message/get/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(
            @DestinationVariable Long chatRoomId) {
        List<ChatMessageResponseDto> response = chatMessageService.getChatMessages(chatRoomId);
        return ResponseEntity.ok(response);
    }

    // 메시지 삭제
    @MessageMapping ("/message/delete/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @DestinationVariable Long messageId,
            @Payload ChatMessageRequestDto message,
            @Header("userId") Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        chatMessageService.deleteMessage(message.getId(), memberId);
        return ResponseEntity.ok("메시지가 삭제되었습니다.");
    }
}
