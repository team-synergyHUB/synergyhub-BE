package com.synergy_hub.synergyhub.config.sessionconfig;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AuthenticationSuccessDto {

    private Long id;
    private String nickname;
    private String email;
    private String password;
    private MemberRole role;
    private LocalDateTime deletedAt;

    public AuthenticationSuccessDto(Member member) {

        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.role = member.getRole();
        this.deletedAt = member.getDeletedAt();

    }
}
