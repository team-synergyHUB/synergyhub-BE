package com.synergy_hub.synergyhub.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TeamMemberResponseDto {  //특정 팀에 속한 멤버 DTO

    private Long id;
    private String nickname;
    private String email;
    private LocalDateTime deletedAt;

    @QueryProjection
    public TeamMemberResponseDto(Long id, String nickname, String email, LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.deletedAt = deletedAt;
    }
}
