package com.synergy_hub.synergyhub.team.dto;

import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.chat.entity.ChatRoom;
import com.synergy_hub.synergyhub.team.entity.Team;
import lombok.Data;

@Data
public class TeamCreateResponseDTO {
    private Long id; // 팀 ID
    private String name; // 팀 이름
    private String inviteCode; // 초대 코드
//    private String inviteSecret; // 초대 비밀번호
    private String labelName; // 라벨 이름

    private Long calendarId; // 캘린더 ID
    private Long roomId; // 채팅 ID

    // 엔티티를 DTO로 변환하는 생성자
    public TeamCreateResponseDTO(Team team, Calendar calendar, ChatRoom chatRoom) {
        this.id = team.getId();
        this.name = team.getName();
        this.inviteCode = team.getInviteCode();
//        this.inviteSecret = team.getInviteSecret();
        this.labelName = team.getLabel() != null ? team.getLabel().getName() : null;

        this.calendarId = calendar.getId();
        this.roomId = chatRoom.getRoomId();
    }
}