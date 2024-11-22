package com.synergy_hub.synergyhub.team.service;

import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.dto.TeamRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Label;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.LabelRepository;
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

    public TeamService(TeamRepository teamRepository, LabelRepository labelRepository) {
        this.teamRepository = teamRepository;
        this.labelRepository = labelRepository;
    }

    // 팀 생성
    public TeamResponseDTO createTeam(TeamRequestDTO request) {
        Label label = null;

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

    // 팀 삭제
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        team.markAsDeleted();
        teamRepository.save(team);
    }

    // 팀 조회
    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAllByIsDeleted(false)
                .stream()
                .map(TeamResponseDTO::new)
                .collect(Collectors.toList());
    }
}
