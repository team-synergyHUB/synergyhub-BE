package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.dto.SuccessResponse;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.mapper.ChatMapper;
import com.synergy_hub.synergyhub.chat.repository.ChatMessageRepository;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatRoomMapper;

    // 채팅방 생성
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(chatRoomRequestDto.getRoomName())
                .roomState(chatRoomRequestDto.getRoomState())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toChatRoomResponseDto(savedChatRoom);
    }

    // 채팅방 목록 조회
    public List<ChatRoomResponseDto> getChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(chatRoomMapper::toChatRoomResponseDto)
                .toList();
    }

    // 채팅방 삭제
    public Long deleteChatRoom(Long chatRoomId) {
        chatMessageRepository.deleteAllByChatRoomId(chatRoomId);
        chatRoomRepository.deleteById(chatRoomId);
        return chatRoomId;
    }
}

