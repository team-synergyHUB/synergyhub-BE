package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LabelResponseDTO {

    @Schema(description = "라벨 ID", example = "1")
    private Long id;

    @Schema(description = "라벨 이름", example = "abcd")
    private String name;

    @Schema(description = "라벨 색상 코드", example = "#FF5733")
    private String color;
}
