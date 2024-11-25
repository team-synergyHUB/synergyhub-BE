package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.team.entity.Label;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LabelDTO {
    @NotBlank(message = "라벨 이름은 필수입니다.")
    @Size(max = 30, message = "라벨 이름은 최대 30자까지 가능합니다.")
    private String name;

    @NotBlank(message = "라벨 색상은 필수입니다.")
    @Size(max = 7, message = "라벨 색상은 최대 7자까지 가능합니다.")
    private String color;

    public LabelDTO(Label label) {
        this.name = label.getName();
        this.color = label.getColor();
    }
}
