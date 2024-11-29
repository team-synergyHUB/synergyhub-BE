package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.team.entity.Team;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamResponseDTO {
    private Long id; // 팀 ID
    private String name; // 팀 이름
    private String inviteCode; // 초대 코드
    private List<LabelDTO> labels; // 라벨 리스트

    // 엔티티를 DTO로 변환하는 생성자
    public TeamResponseDTO(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.inviteCode = team.getInviteCode();
        this.labels = team.getLabels().stream()
                .map(LabelDTO::new) // 라벨 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
}
