package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomRequestDto;
import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
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
    private final TeamRepository teamRepository;

    // 채팅방 생성
    @Transactional
    public ChatRoomResponseDto createChatRoom(Long teamId) {
        // Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀이 존재하지 않습니다."));

        // 이미 채팅방이 생성된 경우 예외 처리
        if (team.getChatRoom() != null) {
            throw new IllegalStateException("이미 채팅방이 생성된 팀입니다.");
        }

        // ChatRoom 빌드 및 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .team(team) // 팀 설정
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 생성된 ChatRoom DTO로 반환
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

