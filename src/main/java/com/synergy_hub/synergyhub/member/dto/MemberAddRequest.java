package com.synergy_hub.synergyhub.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberAddRequest {

    private String nickname;
    private String email;
    private String password;
}
