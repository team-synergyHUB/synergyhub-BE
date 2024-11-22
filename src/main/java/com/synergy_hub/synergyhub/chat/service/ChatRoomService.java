package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto) {
        ChatRoom chatRoom = new ChatRoom(
                null,
                dto.getTeamId(),
                dto.getRoomName(),
                dto.getRoomState(),
                null,
                false
        );
        chatRoom = chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(
                chatRoom.getChatRoomId(),
                chatRoom.getRoomName(),
                chatRoom.getRoomState(),
                chatRoom.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }

    public List<ChatRoomResponseDto> getChatRoomsByTeamId(Long teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatRoomRepository.findByTeamIdAndIsDeletedFalse(teamId)
                .stream()
                .map(chatRoom -> new ChatRoomResponseDto(
                        chatRoom.getChatRoomId(),
                        chatRoom.getRoomName(),
                        chatRoom.getRoomState(),
                        chatRoom.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                ))
                .collect(Collectors.toList());
    }

    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));
        chatRoom.setDeleted(true);
        chatRoomRepository.save(chatRoom);
    }
}
