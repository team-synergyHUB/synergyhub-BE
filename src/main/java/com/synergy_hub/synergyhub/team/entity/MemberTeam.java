package com.synergy_hub.synergyhub.team.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member_team")
public class MemberTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 멤버 ID

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // 연결된 팀
}
