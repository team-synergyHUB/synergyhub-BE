package com.synergy_hub.synergyhub.member.dto;

import com.synergy_hub.synergyhub.member.entity.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import com.synergy_hub.synergyhub.member.entity.Member;
import lombok.Data;

@Data
public class MemberResponseDto {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 닉네임", example = "team_leader")
    private String nickname;

    @Schema(description = "회원 이메일 주소", example = "leader@example.com")
    private String email;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile/leader.png")
    private String profileImageUrl;

    private LoginType loginType;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profileImageUrl = member.getProfileImageUrl();
        this.loginType = member.getLoginType();
    }
}
