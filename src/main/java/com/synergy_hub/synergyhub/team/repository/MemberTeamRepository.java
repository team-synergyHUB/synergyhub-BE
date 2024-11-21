package com.synergy_hub.synergyhub.team.repository;

import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    // 특정 팀에 속한 모든 멤버 ID 조회
    List<MemberTeam> findAllByTeam(Team team);

    // 특정 멤버가 속한 팀-멤버 관계 삭제
    void deleteByMemberId(Long memberId);
}
