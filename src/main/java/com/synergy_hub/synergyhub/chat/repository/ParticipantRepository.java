package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByChatRoom_ChatRoomId(Long chatRoomId);
    void deleteByChatRoom_ChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}

