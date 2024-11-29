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
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    // 메시지 전송
    @MessageMapping("/message/send/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequestDto requestDto,
            @Header("user") Principal principal) {
        Long memberId = Long.valueOf(principal.getName()); // Principal에서 memberId를 가져온다고 가정
        ChatMessageResponseDto createdMessage = chatMessageService.sendMessage(chatRoomId, requestDto, memberId);

        // 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/messages/" + chatRoomId, createdMessage);
    }

    // 메시지 조회
    @MessageMapping("/message/get/{chatRoomId}")
    public void getChatMessages(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Header("user") Principal principal) {
        List<ChatMessageResponseDto> messages = chatMessageService.getChatMessages(chatRoomId);

        // 조회된 메시지 목록을 구독자에게 전달
        messagingTemplate.convertAndSend("/topic/messages/" + chatRoomId, messages);
    }

    // 메시지 삭제
    @MessageMapping("/message/delete/{chatRoomId}")
    public void deleteMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequestDto requestDto,
            @Header("user") Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        Long deletedMessageId = chatMessageService.deleteMessage(requestDto.getId(), memberId);

        // 삭제된 메시지 ID를 브로드캐스트
        messagingTemplate.convertAndSend("/topic/message-deletions/" + chatRoomId, deletedMessageId);
    }
}
