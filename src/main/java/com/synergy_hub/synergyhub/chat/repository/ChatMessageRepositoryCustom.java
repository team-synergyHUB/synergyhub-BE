package com.synergy_hub.synergyhub.chat.repository;

import com.synergy_hub.synergyhub.chat.dto.ChatMessageResponseDto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepositoryCustom {

    //기존에 참가, 새로 참가한 회원인지 검증
    boolean hasJoined(Long memberId, Long chatRoomId);

    //특정 채팅방 메세지 조회 - 채팅방 입장 시간 기준으로
    List<ChatMessageResponseDto> findHistoryByChatRoomId(Long memberId, Long chatRoomId);

    // 채팅 참가 (ENTER) 타입 검증
    boolean hasEnterType(Long memberId, Long chatRoomId);

}
