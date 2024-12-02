package com.synergy_hub.synergyhub.team.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LabelResponseDTO {
    private Long id;      // 라벨 ID
    private String name;  // 라벨 이름
    private String color; // 라벨 색상
}
