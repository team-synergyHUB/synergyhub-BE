package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    // 특정 팀에 속한 모든 멤버 ID 조회
    List<MemberTeam> findAllByTeam(Team team);

    // 특정 멤버가 속한 팀-멤버 관계 삭제
    void deleteByMemberId(Long memberId);

    // 멤버 ID와 팀 정보를 기반으로 관계를 삭제
    void deleteByMemberIdAndTeam(Long memberId, Team team);

    // 특정 멤버가 특정 팀에 속해 있는지 찾기
    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);

    // 특정 팀에 멤버가 남아 있는지 확인
    boolean existsByTeamId(Long teamId);

    boolean existsByTeamAndMemberId(Team team, Long memberId);

    boolean existsByTeamAndMember(Team team, Member member);

    // 특정 팀에 멤버가 남아 있는지 확인
    boolean existsByTeam(Team team);

    // 사용자가 소속된 모든 팀 조회
    List<MemberTeam> findAllByMemberId(Long memberId);



}
