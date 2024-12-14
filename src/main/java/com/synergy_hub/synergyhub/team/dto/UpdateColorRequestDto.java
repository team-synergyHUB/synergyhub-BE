package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateColorRequestDto {

    @Schema(description = "팀 ID", example = "101")
    private Long teamId;

    @Schema(description = "변경할 색상 코드", example = "#FF5733")
    private String newColor;
}