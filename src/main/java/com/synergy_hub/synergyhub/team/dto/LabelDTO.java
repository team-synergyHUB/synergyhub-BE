package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.team.entity.Label;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LabelDTO {
    private String name; // 라벨 이름
    private String color; // 라벨 색상

    // 라벨 엔티티를 DTO로 변환하는 생성자
    public LabelDTO(Label label) {
        this.name = label.getName();
        this.color = label.getColor();
    }
}

