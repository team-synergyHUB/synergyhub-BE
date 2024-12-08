package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.team.entity.Label;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LabelRequestDTO {
    private String name; // 라벨 이름
    private String color; // 라벨 색상

    // 라벨 엔티티를 DTO로 변환하는 생성자
    public LabelRequestDTO(Label label) {
        this.name = label.getName();
        this.color = label.getColor();
    }
}

