package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.synergy_hub.synergyhub.team.entity.Team;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamResponseDTO {

    @Schema(description = "팀 ID", example = "101")
    private Long id;

    @Schema(description = "팀 이름", example = "Synergy Team")
    private String name;

    @Schema(description = "초대 코드", example = "XYZ123")
    private String inviteCode;

    @Schema(description = "라벨 리스트", example = "[{\"name\": \"abcd\", \"color\": \"#FF5733\"}]")
    private List<LabelRequestDTO> labels;

    // 엔티티를 DTO로 변환하는 생성자
    public TeamResponseDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.inviteCode = team.getInviteCode();
        this.labels = team.getLabels().stream()
            .map(LabelRequestDTO::new) // 라벨 엔티티를 DTO로 변환
            .collect(Collectors.toList());
    }
}
