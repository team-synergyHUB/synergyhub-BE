package com.synergy_hub.synergyhub.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private Long roomId;    // 채팅방 ID
    private String memberId; // 회원 ID
}
