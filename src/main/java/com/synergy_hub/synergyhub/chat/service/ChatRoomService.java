package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.dto.ChatRoomResponseDto;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.mapper.ChatMapper;
import com.synergy_hub.synergyhub.chat.repository.ChatMessageRepository;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 이미 채팅방이 생성된 경우 예외 처리
        if (team.getChatRoom() != null) {
            throw new CustomException(ErrorCode.DUPLICATE_CHAT_ROOM);
        }

        // ChatRoom 빌드 및 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .team(team) // 팀 설정
                .createdAt(LocalDateTime.now()) // TODO : CREATEDDATE
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

    // 팀 ID로 채팅방 정보 가져오기
    @Transactional(readOnly = true)
    public ChatRoomResponseDto getChatRoomByTeamId(Long teamId) {
        // Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // Team에 연결된 ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findByTeam(team)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // ChatRoom -> ChatRoomResponseDto 매핑
        return chatRoomMapper.toChatRoomResponseDto(chatRoom);
    }

    // 채팅방 삭제
    @Transactional
    public ChatRoom deleteChatRoom(Long chatRoomId) {
        // 채팅 메시지 삭제
        chatMessageRepository.deleteAllByChatRoomId(chatRoomId);

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅방 삭제
        chatRoomRepository.delete(chatRoom);
        return chatRoom; // 삭제된 엔티티 반환
    }
}
