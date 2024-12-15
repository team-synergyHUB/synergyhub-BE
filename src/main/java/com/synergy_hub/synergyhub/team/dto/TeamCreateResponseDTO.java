package com.synergy_hub.synergyhub.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.team.entity.Team;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamCreateResponseDTO {

    @Schema(description = "팀 ID", example = "101")
    private Long id;

    @Schema(description = "팀 이름", example = "Synergy Team")
    private String name;

    @Schema(description = "팀 초대 코드", example = "ABC123")
    private String inviteCode;

    @Schema(description = "라벨 리스트", example = "[{\"name\": \"abcd\", \"color\": \"#FF5733\"}]")
    private List<LabelRequestDTO> labels;

    @Schema(description = "캘린더 ID", example = "10")
    private Long calendarId;

    @Schema(description = "채팅방 ID", example = "5")
    private Long roomId;

    @Schema(description = "현재 사용자의 멤버 ID", example = "20")
    private Long memberId;

    // 엔티티를 DTO로 변환하는 생성자
    public TeamCreateResponseDTO(Team team, Calendar calendar, ChatRoom chatRoom, Long memberId) {
        this.id = team.getId();
        this.name = team.getName();
        this.inviteCode = team.getInviteCode();

        // 라벨 리스트를 DTO로 변환
        this.labels = team.getLabels().stream()
            .map(LabelRequestDTO::new)
            .collect(Collectors.toList());

        this.calendarId = calendar.getId();
        this.roomId = chatRoom.getRoomId();

        this.memberId = memberId; // 멤버 ID 설정
    }
}
