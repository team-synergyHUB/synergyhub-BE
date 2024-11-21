package com.synergy_hub.synergyhub.member.entity;

import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams;

    //특정 팀에 참여
    public void joinTeam(Team team) {

        MemberTeam memberTeam = new MemberTeam();
//        memberTeam.setTeamAndMember(team, this);

    }

    //연관 관계 편의 메서드
    public void addMemberTeams(MemberTeam memberTeam) {
        this.memberTeams.add(memberTeam);
    }

    private Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static Member createMember(String nickname, String email, String password) {
        return new Member(nickname, email, password);
    }

}
