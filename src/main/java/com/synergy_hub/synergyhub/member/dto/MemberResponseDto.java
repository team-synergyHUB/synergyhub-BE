package com.synergy_hub.synergyhub.member.dto;

import com.synergy_hub.synergyhub.member.entity.Member;
import lombok.Data;

@Data
public class MemberResponseDto {

    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profileImageUrl = member.getProfileImageUrl();
    }
}
