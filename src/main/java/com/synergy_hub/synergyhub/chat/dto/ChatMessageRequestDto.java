package com.synergy_hub.synergyhub.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.synergy_hub.synergyhub.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageRequestDto {
    private Long id;                      // 메시지 ID 추가
    private ChatMessageContent message;              // 메시지 내용
//    private String message;
    private ChatMessage.MessageType type; // 메시지 타입 (ENTER, TALK, QUIT 등)
}
