package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.team.entity.Label;
import lombok.Data;

@Data
public class LabelDTO {
    private Long id; // 라벨 ID
    private String name; // 라벨 이름
    private String color; // 라벨 색상

    // 엔티티를 DTO로 변환하는 생성자
    public LabelDTO(Label label) {
        this.id = label.getId();
        this.name = label.getName();
        this.color = label.getColor();
    }
}