package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomRequestDto dto) {
        ChatRoomResponseDto response = chatRoomService.createChatRoom(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<List<ChatRoomResponseDto>> getChatRoomsByTeamId(
            @PathVariable Long teamId,
            @RequestParam int page,
            @RequestParam int size) {
        List<ChatRoomResponseDto> chatRooms = chatRoomService.getChatRoomsByTeamId(teamId, page, size);
        return ResponseEntity.ok(chatRooms);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok("Chatroom deleted successfully");
    }

    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<String> joinChatRoom(@PathVariable Long chatRoomId, WebSocketSession session) {
        chatRoomService.addSession(chatRoomId, session);
        return ResponseEntity.ok("Joined chat room");
    }

    @PostMapping("/{chatRoomId}/leave")
    public ResponseEntity<String> leaveChatRoom(@PathVariable Long chatRoomId, WebSocketSession session) {
        chatRoomService.removeSession(chatRoomId, session);
        return ResponseEntity.ok("Left chat room");
    }
}
