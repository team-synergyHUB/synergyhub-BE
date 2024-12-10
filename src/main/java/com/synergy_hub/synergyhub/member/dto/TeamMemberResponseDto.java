package com.synergy_hub.synergyhub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TeamMemberResponseDto {  //특정 팀에 속한 멤버 DTO

    @Schema(description = "팀 멤버 ID", example = "1")
    private Long id;

    @Schema(description = "팀 멤버 닉네임", example = "team_member")
    private String nickname;

    @Schema(description = "팀 멤버 이메일 주소", example = "member@example.com")
    private String email;

    @Schema(description = "멤버 삭제 시간 (삭제되지 않은 경우 null)", example = "null")
    private LocalDateTime deletedAt;

    @QueryProjection
    public TeamMemberResponseDto(Long id, String nickname, String email, LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.deletedAt = deletedAt;
    }
}
