package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class LabelMappingRequestDTO {

    @Schema(description = "라벨 ID 리스트", example = "[1]")
    private List<Long> labelIds;
}
