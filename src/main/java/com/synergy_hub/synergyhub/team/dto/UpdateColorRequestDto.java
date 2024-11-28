package com.synergy_hub.synergyhub.team.dto;

import lombok.Data;

@Data
public class UpdateColorRequestDto {
    private Long memberId;
    private Long teamId;
    private String newColor;

}
