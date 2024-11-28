package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 팀의 채팅방 조회
    Optional<ChatRoom> findByTeam(Team team);

    // 모든 채팅방 조회 (삭제되지 않은 경우만)
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.deletedAt IS NULL")
    List<ChatRoom> findAllActive();

    // 특정 채팅방 Soft Delete
    @Modifying
    @Query("UPDATE ChatRoom cr SET cr.deletedAt = CURRENT_TIMESTAMP WHERE cr.id = :chatRoomId")
    void softDeleteById(@Param("chatRoomId") Long chatRoomId);
}
