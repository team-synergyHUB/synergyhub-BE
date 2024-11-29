package com.synergy_hub.synergyhub.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDto {
    private String roomName;   // 채팅방 이름
    private String roomState; // 채팅방 상태 (예: 공개, 비공개 등)
}


