package com.synergy_hub.synergyhub.chat.service;

import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.chat.repository.ChatRoomRepository;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final TeamRepository teamRepository;

    /**
     * 채팅방 생성
     */
    @Transactional
    public ChatRoom createChatRoom(Long teamId, String roomName, String roomState) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        ChatRoom chatRoom = new ChatRoom(
                null,
                team,
                roomName,
                roomState,
                LocalDateTime.now(),
                null
        );

        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 모든 채팅방 조회 (삭제되지 않은 것만)
     */
    @Transactional(readOnly = true)
    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAllByDeletedAtIsNull();
    }

    /**
     * 특정 채팅방 조회
     */
    @Transactional(readOnly = true)
    public ChatRoom findChatRoomById(Long roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
    }

    /**
     * 채팅방 삭제
     */
    @Transactional
    public void deleteChatRoom(Long roomId) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        chatRoom.delete();
        chatRoomRepository.save(chatRoom);
    }
}
