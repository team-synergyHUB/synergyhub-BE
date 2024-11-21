package com.synergy_hub.synergyhub.member.dto;

import lombok.Data;

@Data
public class AddMemberRequest {

    private String nickname;
    private String email;
    private String password;
}
