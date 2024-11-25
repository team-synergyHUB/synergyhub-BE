package com.synergy_hub.synergyhub.chat.controller;

import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 모든 채팅방 조회
     */
    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    /**
     * 특정 채팅방 조회
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoomById(@PathVariable Long roomId) {
        ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 채팅방 생성
     */
    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long teamId,
                                                   @RequestParam String roomName,
                                                   @RequestParam String roomState) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(teamId, roomName, roomState);
        return ResponseEntity.ok(chatRoom);
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }
}
