package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.team.entity.Team;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamCreateResponseDTO {
    private Long id; // 팀 ID
    private String name; // 팀 이름
    private String inviteCode; // 초대 코드
    private List<LabelRequestDTO> labels; // 라벨 리스트

    private Long calendarId; // 캘린더 ID
    private Long roomId; // 채팅 ID

    private boolean isMember; // 생성된 사용자가 이 팀의 멤버인지 여부

    // 엔티티를 DTO로 변환하는 생성자
    public TeamCreateResponseDTO(Team team, Calendar calendar, ChatRoom chatRoom, boolean isMember) {
        this.id = team.getId();
        this.name = team.getName();
        this.inviteCode = team.getInviteCode();

        // 라벨 리스트를 DTO로 변환
        this.labels = team.getLabels().stream()
                .map(LabelRequestDTO::new)
                .collect(Collectors.toList());

        this.calendarId = calendar.getId();
        this.roomId = chatRoom.getRoomId();

        this.isMember = isMember;
    }
}