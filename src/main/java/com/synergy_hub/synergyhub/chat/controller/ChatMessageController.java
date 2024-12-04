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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;


    // 메시지 전송
    @MessageMapping("/chat/message/sendMessage/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequestDto requestDto
    ) {
        // 요청 데이터 로깅
        System.out.println("Received ChatMessageRequestDto: " + requestDto);

        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getName(); // Principal에서 memberId를 가져온다고 가정
        ChatMessageResponseDto createdMessage = chatMessageService.sendMessage(chatRoomId, requestDto, email);


        // 응답 데이터 로깅
        System.out.println("Created ChatMessageResponseDto: " + createdMessage);

        System.out.println("CM = " + createdMessage);
        System.out.println("rqDTO = " + requestDto);

        // 생성된 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/messages/" + chatRoomId, createdMessage);
    }

    // 메시지 삭제
    @MessageMapping("/chat/message/deleteMessage/{chatRoomId}")
    public void deleteMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequestDto requestDto) {
        // 현재 사용자의 ID 가져오기
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getName(); // Principal에서 memberId를 가져온다고 가정

        Long deletedMessageId = chatMessageService.deleteMessage(requestDto.getId(), email);

        // 삭제된 메시지 ID를 브로드캐스트
        messagingTemplate.convertAndSend("/topic/message-deletions/" + chatRoomId, deletedMessageId);
    }

//        // 채팅방 별 메시지 조회
//    @MessageMapping("/chat/message/getMessagesByRoom/{chatRoomId}")
//    public void getMessagesByRoom(
//            @DestinationVariable("chatRoomId") Long chatRoomId,
//            @Header("user") Principal principal) {
//        // 채팅방 메시지 조회
//        List<ChatMessageResponseDto> messages = chatMessageService.getChatMessages(chatRoomId);
//
//        // 조회된 메시지를 브로드캐스트
//        messagingTemplate.convertAndSend("/topic/messages-room/" + chatRoomId, messages);
//    }
//
//    // 메시지 ID로 메시지 조회
//    @MessageMapping("/chat/message/getMessageById/{messageId}")
//    public void getMessageById(
//            @DestinationVariable("messageId") Long messageId,
//            @Header("user") Principal principal) {
//        // 특정 메시지 조회
//        ChatMessageResponseDto message = chatMessageService.getMessageById(messageId);
//
//        // 조회된 메시지를 브로드캐스트
//        messagingTemplate.convertAndSend("/topic/message-id/" + messageId, message);
//    }


    /**
     * 특정 채팅방의 모든 메시지 조회 (REST API 방식)
     */
    @GetMapping("/chat/message/history/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(
            @PathVariable("chatRoomId") Long chatRoomId) {
        List<ChatMessageResponseDto> chatMessages = chatMessageService.getChatMessages(chatRoomId);
        return ResponseEntity.ok(chatMessages);
    }

    /**
     * 특정 메시지 조회 (REST API 방식)
     */
    @GetMapping("/message/{messageId}")
    public ResponseEntity<ChatMessageResponseDto> getMessageById(
            @PathVariable("messageId") Long messageId) {
        ChatMessageResponseDto message = chatMessageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }


}
