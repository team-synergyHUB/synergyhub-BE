package com.synergy_hub.synergyhub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberLoginRequest {

    @Schema(description = "회원 로그인 ID(이메일)", example = "leader@example.com")
    private String username;

    @Schema(description = "회원 비밀번호", example = "password123")
    private String password;
}
