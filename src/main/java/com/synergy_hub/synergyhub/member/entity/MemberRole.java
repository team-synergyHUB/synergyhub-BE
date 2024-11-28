package com.synergy_hub.synergyhub.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    MemberRole(String value) {
        this.value = value;
    }

    // String 값으로 MemberRole 찾기
    public static MemberRole fromString(String role) {
        for (MemberRole memberRole : MemberRole.values()) {
            if (memberRole.getValue().contains(role)) {
                return memberRole;
            }
        }
        throw new IllegalArgumentException("Unexpected role: " + role);
    }

    private String value;

}
