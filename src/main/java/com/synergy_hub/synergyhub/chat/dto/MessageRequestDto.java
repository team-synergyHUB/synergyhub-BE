package com.synergy_hub.synergyhub.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    private Long memberId;
    private String type; // ENUM: TEXT or IMAGE
    private String detailMessage;
}
