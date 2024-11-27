package com.synergy_hub.synergyhub.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column
    private MemberRole role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

//    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTeam> memberTeams = new ArrayList<>();

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

    //특정 팀에 참여
    public MemberTeam joinTeam(Team team) {
        MemberTeam memberTeam = new MemberTeam(this, team);
        addMemberTeams(memberTeam);
        return memberTeam;
    }

    //특정 팀에서 나가기
    public void leaveTeam(Long teamId) {
        MemberTeam memberTeam = memberTeams.stream()
            .filter(mt -> mt.getTeam().getId().equals(teamId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("team not found"));

        memberTeams.remove(memberTeam);
    }

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public void deleteAccount() {
        deletedAt = LocalDateTime.now(); // 탈퇴 시 현재 시간 저장
    }

    public boolean isDeleted() {
        return deletedAt != null; // 탈퇴 여부 확인
    }

}
