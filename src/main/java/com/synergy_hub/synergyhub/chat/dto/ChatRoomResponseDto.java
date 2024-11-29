package com.synergy_hub.synergyhub.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponseDto {
    private Long roomId;        // 채팅방 ID
//    private String roomName;    // 채팅방 이름
    private String roomState;   // 채팅방 상태
    private String teamName;    // 연결된 팀 이름
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime deletedAt;  // 삭제 여부
}
