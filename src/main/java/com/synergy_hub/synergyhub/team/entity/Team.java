package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false, updatable = false)
    private Long id; // 팀 ID

    @Column(name = "name", nullable = false, length = 255)
    private String name; // 팀 이름

    @Column(name = "invite_code", nullable = false, length = 8, unique = true)
    private String inviteCode; // 초대 코드

    @Column(name = "invite_secret", nullable = false, length = 255)
    private String inviteSecret; // 초대 비밀번호

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted; // 팀 삭제 여부 (소프트 삭제)

    @Column(name = "labels", columnDefinition = "JSON")
    private String labels; // JSON 형식의 라벨
}

