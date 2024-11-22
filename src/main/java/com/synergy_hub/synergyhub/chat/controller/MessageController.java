package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.MessageRequestDto;
import com.synergy_hub.synergyhub.chat.dto.MessageResponseDto;
import com.synergy_hub.synergyhub.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatrooms/{chatRoomId}/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(
            @PathVariable Long chatRoomId,
            @RequestBody MessageRequestDto dto) {
        MessageResponseDto response = messageService.sendMessage(chatRoomId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> getMessages(
            @PathVariable Long chatRoomId,
            @RequestParam int page,
            @RequestParam int size) {
        List<MessageResponseDto> messages = messageService.getMessages(chatRoomId, page, size);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long chatRoomId, @PathVariable Long messageId) {
        messageService.deleteMessage(chatRoomId, messageId);
        return ResponseEntity.ok("Message deleted successfully");
    }
}
