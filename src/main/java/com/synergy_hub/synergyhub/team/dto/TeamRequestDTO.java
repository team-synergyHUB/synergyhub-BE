package com.synergy_hub.synergyhub.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamRequestDTO {
    @NotBlank(message = "팀 이름은 필수 입력 항목입니다.")
    private String name; // 팀 이름

    private List<Long> labelIds; // 여러 라벨 ID

    private Boolean isDeleted = false; // 기본값 설정
}
