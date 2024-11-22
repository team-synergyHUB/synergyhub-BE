package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByTeamIdAndIsDeletedFalse(Long teamId);


}
