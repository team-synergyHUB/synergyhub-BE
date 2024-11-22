package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class MemberTeamService {
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;

    public MemberTeamService(MemberTeamRepository memberTeamRepository, TeamRepository teamRepository) {
        this.memberTeamRepository = memberTeamRepository;
        this.teamRepository = teamRepository;
    }

    // 팀에 멤버 추가
    public void addMemberToTeam(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        MemberTeam memberTeam = new MemberTeam(memberId, team);
        memberTeamRepository.save(memberTeam);
    }

    // 팀에서 멤버 제거
    public void removeMemberFromTeam(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        memberTeamRepository.deleteByMemberIdAndTeam(memberId, team);
    }

    // 특정 팀의 멤버 목록 조회
    public List<Long> getMembersOfTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        return memberTeamRepository.findAllByTeam(team)
                .stream()
                .map(MemberTeam::getMemberId)
                .toList();
    }
}