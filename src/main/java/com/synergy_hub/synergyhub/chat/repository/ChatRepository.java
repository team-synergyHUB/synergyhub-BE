package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.entity.Chat;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 특정 채팅방과 회원으로 Chat 조회
    Optional<Chat> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    // 특정 회원이 참여한 채팅 목록 조회
    List<Chat> findByMember(Member member);

    // 특정 채팅방에 참여한 모든 Chat 조회
    List<Chat> findByChatRoom(ChatRoom chatRoom);
}

