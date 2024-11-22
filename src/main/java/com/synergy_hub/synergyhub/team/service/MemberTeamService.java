package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        MemberTeam memberTeam = new MemberTeam(memberId, team);
        memberTeamRepository.save(memberTeam);
    }

    // 팀에서 멤버 제거
    public void removeMemberFromTeam(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

//        // 멤버 검증 및 삭제
//        int deletedCount = memberTeamRepository.deleteByMemberIdAndTeam(memberId, team);
//        if (deletedCount == 0) {
//            throw new CustomException(ErrorCode.MEMBER_NOT_IN_TEAM);
//        }

        memberTeamRepository.deleteByMemberIdAndTeam(memberId, team);
    }

    // 특정 팀의 멤버 목록 조회
    public List<Long> getMembersOfTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        return memberTeamRepository.findAllByTeam(team)
                .stream()
                .map(MemberTeam::getMemberId)
                .toList();
    }
}