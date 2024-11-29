package com.synergy_hub.synergyhub.chat.dto;

import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDto {
    private Long messageId;               // 메시지 ID
    private Long roomId;                  // 채팅방 ID
//    private String roomName;              // 채팅방 이름
    private String nickname;        // 보낸 사람 닉네임
    private String message;               // 메시지 내용
    private ChatMessage.MessageType type; // 메시지 타입 (ENTER, TALK, QUIT 등)
    private LocalDateTime createdAt;      // 메시지 생성 시간
    private LocalDateTime deletedAt;      // 삭제된 시간 (삭제되지 않은 경우 null)
}

