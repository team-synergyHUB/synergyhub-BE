package com.synergy_hub.synergyhub.team.entity;

import com.synergy_hub.synergyhub.member.entity.Member;
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

//    @Column(name = "member_id", nullable = false)
//    private Long memberId; // 멤버 ID

    @Column(nullable = false, length = 7)
    private String color; // 팀 색상

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // 연결된 팀

    @ManyToOne
//    @Column(name = "member_id", nullable = false)
    @JoinColumn(name = "member_id")
    private Member member; // 멤버 ID

    // 커스텀 생성자 추가
    public MemberTeam(Member member, Team team) {
        this.member = member;
        this.team = team;
    }
}
