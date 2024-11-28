package com.synergy_hub.synergyhub.chat.dto;

import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private String message;               // 메시지 내용
    private ChatMessage.MessageType type; // 메시지 타입 (ENTER, TALK, QUIT 등)
}
