package com.synergy_hub.synergyhub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberAddRequest {

    @Schema(description = "회원 닉네임", example = "team_leader")
    private String nickname;

    @Schema(description = "회원 이메일 주소", example = "leader@example.com")
    private String email;

    @Schema(description = "회원 비밀번호", example = "password123")
    private String password;
}
