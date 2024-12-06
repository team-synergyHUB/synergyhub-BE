package com.synergy_hub.synergyhub.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 초대 코드 응답 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteCodeResponseDTO {
    private String inviteCode;
}
