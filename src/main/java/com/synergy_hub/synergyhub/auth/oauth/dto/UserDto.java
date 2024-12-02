package com.synergy_hub.synergyhub.auth.oauth.dto;

import com.synergy_hub.synergyhub.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String uid;
    private String email;
    private String name;
    private MemberRole role;
}
