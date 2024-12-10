package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteCodeResponseDTO {

    @Schema(description = "팀 초대 코드", example = "XYZ123")
    private String inviteCode;
}
