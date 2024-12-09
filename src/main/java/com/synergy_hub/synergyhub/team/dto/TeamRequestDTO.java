package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamRequestDTO {

    @Schema(description = "팀 이름", example = "Synergy Team")
    @NotBlank(message = "팀 이름은 필수 입력 항목입니다.")
    private String name;

    @Schema(description = "라벨 ID 리스트", example = "[1, 2, 3]")
    private List<Long> labelIds;

    @Schema(description = "삭제 여부 (기본값: false)", example = "false", defaultValue = "false")
    private Boolean isDeleted = false;
}
