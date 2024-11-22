package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.TeamRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.entity.MemberTeam;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final LabelRepository labelRepository;
    private final MemberTeamRepository memberTeamRepository;

    public TeamService(TeamRepository teamRepository, LabelRepository labelRepository, MemberTeamRepository memberTeamRepository) {
        this.teamRepository = teamRepository;
        this.labelRepository = labelRepository;
        this.memberTeamRepository = memberTeamRepository;
    }

    // 팀 생성
    public TeamResponseDTO createTeam(TeamRequestDTO request) {
        Label label = null;

        // 깃랩 label처럼 구현 계획
        if (request.getLabelId() != null) {
            label = labelRepository.findById(request.getLabelId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));
        }

        Team team = Team.builder()
                .name(request.getName())
                .inviteCode(request.getInviteCode())
                .inviteSecret(request.getInviteSecret())
                .label(label)
                .build();

        Team savedTeam = teamRepository.save(team);
        return new TeamResponseDTO(savedTeam);
    }

    // 팀 수정
    public TeamResponseDTO updateTeam(Long teamId, TeamRequestDTO request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Label label = null;
        if (request.getLabelId() != null) {
            label = labelRepository.findById(request.getLabelId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LABEL_NOT_FOUND));
        }

        team.updateTeam(request.getName(), request.getInviteCode(), request.getInviteSecret(), label);

        return new TeamResponseDTO(team);
    }

    // 팀 삭제 : 팀에서 모든 멤버가 나가면 팀 삭제
    public void leaveTeam(Long memberId, Long teamId) {
        // 해당 멤버와 팀의 연결 정보(MemberTeam) 찾기
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TEAM_NOT_FOUND));

        // 연결 정보 삭제 (팀에서 나가기)
        //memberTeamRepository.delete(memberTeam);

        // 팀에 남아 있는 멤버가 있는지 확인
        boolean hasRemainingMembers = memberTeamRepository.existsByTeamId(teamId);

        // 팀원이 없으면 팀 삭제
        if (!hasRemainingMembers) {
            Team team = memberTeam.getTeam();
            team.markAsDeleted();
            teamRepository.save(team);
        }
    }

    // 팀 조회
    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAllByIsDeleted(false)
                .stream()
                .map(TeamResponseDTO::new)
                .collect(Collectors.toList());
    }
}
