package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 메시지 조회
    List<ChatMessage> findByChatRoom_RoomId(Long roomId);

    // 특정 메시지 Soft Delete
    @Modifying
    @Query("UPDATE ChatMessage cm SET cm.deletedAt = CURRENT_TIMESTAMP WHERE cm.id = :messageId")
    void softDeleteById(@Param("messageId") Long messageId);

    // 특정 채팅방의 모든 메시지 삭제
    @Modifying
    @Query("DELETE FROM ChatMessage cm WHERE cm.chatRoom.roomId = :chatRoomId")
    void deleteAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
