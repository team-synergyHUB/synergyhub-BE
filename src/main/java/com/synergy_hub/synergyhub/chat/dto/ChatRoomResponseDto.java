package com.synergy_hub.synergyhub.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private String roomName;
    private String roomState;
    private String createdAt;
}
