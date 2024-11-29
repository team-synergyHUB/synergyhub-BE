package com.synergy_hub.synergyhub.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDto {
    private Long id;          // Chat ID
    private Long roomId;      // 채팅방 ID
    private String roomName;  // 채팅방 이름
    private String memberName; // 회원 이름
    private LocalDateTime createdAt; // 참여 시간
}
