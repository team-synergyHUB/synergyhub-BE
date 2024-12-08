package com.synergy_hub.synergyhub.team.dto;

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
    @NotBlank(message = "초대 코드는 필수 입력값입니다.")
    private String inviteCode;
}
