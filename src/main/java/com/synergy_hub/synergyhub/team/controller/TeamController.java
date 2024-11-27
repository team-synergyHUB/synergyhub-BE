package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.config.global.SwaggerDocumentation;
import com.synergy_hub.synergyhub.team.dto.TeamCreateResponseDTO;
import com.synergy_hub.synergyhub.team.dto.TeamRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 관련 API") // Swagger 태그
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다.")
    @SwaggerDocumentation.CreateTeamResponses // 공통 응답 사용
    @PostMapping
    public ResponseEntity<TeamCreateResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO request) {
        TeamCreateResponseDTO response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "팀 수정", description = "기존 팀의 정보를 수정합니다.")
    @SwaggerDocumentation.UpdateTeamResponses // 공통 응답 사용
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDTO request) {
        TeamResponseDTO response = teamService.updateTeam(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "팀 나가기", description = "팀에서 멤버를 제거합니다. 모든 멤버가 나가면 팀은 삭제됩니다.")
    @SwaggerDocumentation.LeaveTeamResponses // 공통 응답 사용
    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> leaveTeam(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        teamService.leaveTeam(memberId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "팀 목록 조회", description = "모든 팀 목록을 조회합니다.")
    @SwaggerDocumentation.CommonResponses // 공통 응답 사용
    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }
}