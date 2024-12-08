package com.synergy_hub.synergyhub.team.dto;

import lombok.Data;

import java.util.List;

@Data
public class LabelMappingRequestDTO {
    private List<Long> labelIds; // 라벨 ID 리스트
}
