package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_team")
public class MemberTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 멤버 ID

    @Column(nullable = false, length = 7)
    private String color; // 팀 색상

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // 연결된 팀

    // 커스텀 생성자 추가
    public MemberTeam(Long memberId, Team team) {
        this.memberId = memberId;
        this.team = team;
    }
}
