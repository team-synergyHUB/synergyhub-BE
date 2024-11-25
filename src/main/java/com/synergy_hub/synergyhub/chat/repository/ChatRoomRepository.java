package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(Long roomId);

    List<ChatRoom> findAllByDeletedAtIsNull(); // 삭제되지 않은 채팅방 조회
}
