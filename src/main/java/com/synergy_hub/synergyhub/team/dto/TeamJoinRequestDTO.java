package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamJoinRequestDTO {

    @Schema(description = "팀 초대 코드", example = "XYZ789")
    @NotBlank(message = "초대 코드는 필수 입력값입니다.")
    private String inviteCode;
}
