package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.config.global.SwaggerDocumentation;
import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.team.dto.*;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 관련 API") // Swagger 태그
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 도메인
public class TeamController {

    private final TeamService teamService;
    private final JwtTokenProvider jwtTokenProvider;

    // 팀에 라벨 매핑
    @CommonApiDocs(summary = "팀에 라벨 매핑", description = "팀에 라벨을 연결합니다.")
    @PostMapping("/{teamId}/labels")
    public ResponseEntity<String> mapLabelsToTeam(
            @PathVariable Long teamId,
            @RequestBody LabelMappingRequestDTO request) {
        teamService.mapLabelsToTeam(teamId, request.getLabelIds());
        return ResponseEntity.ok("Labels successfully mapped to team.");
    }

    @PostMapping
    public ResponseEntity<TeamCreateResponseDTO> createTeam(
            @Valid @RequestBody TeamRequestDTO request,
            @RequestHeader("Authorization") String authorizationHeader) {

        System.out.println("1. 요청 수신: " + request); // 요청 데이터 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }

        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long memberId = jwtTokenProvider.getUserId(token);
        System.out.println("2. 추출된 사용자 ID: " + memberId); // 사용자 ID 확인

        TeamCreateResponseDTO createdTeam = teamService.createTeamWithMember(request, memberId);
        System.out.println("3. 생성된 팀: " + createdTeam); // 팀 생성 성공 여부 확인

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    // 팀 수정
    @CommonApiDocs(summary = "팀 수정", description = "기존 팀의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDTO request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    // 팀 나가기
    @CommonApiDocs(summary = "팀 나가기", description = "팀에서 멤버를 제거합니다. 모든 멤버가 나가면 팀은 삭제됩니다.")
    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> leaveTeam(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        teamService.leaveTeam(memberId, id);
        return ResponseEntity.noContent().build();
    }

    // 모든 팀 조회 (페이지네이션 포함)
    @CommonApiDocs(summary = "팀 목록 조회", description = "모든 팀 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<TeamResponseDTO>> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getAllTeams(page, size));
    }

    @GetMapping("/member")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByLoggedInMember(
            @RequestHeader("Authorization") String authorizationHeader) {

        // 1. Authorization 헤더 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }

        // 2. 토큰에서 사용자 ID 추출
        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long memberId = jwtTokenProvider.getUserId(token); // 토큰에서 사용자 ID 추출
        System.out.println("요청한 사용자 ID: " + memberId); // 로그로 확인

        // 3. 사용자 ID에 속한 팀 조회
        List<TeamResponseDTO> teams = teamService.getTeamsByMember(memberId);

        // 4. 조회 결과 반환
        return ResponseEntity.ok(teams);
    }

    // 초대 코드 조회 API
    @GetMapping("/{teamId}/invite-code")
    public ResponseEntity<InviteCodeResponseDTO> getInviteCode(@PathVariable Long teamId) {
        String inviteCode = teamService.getInviteCodeByTeamId(teamId);
        InviteCodeResponseDTO responseDto = new InviteCodeResponseDTO(inviteCode);
        return ResponseEntity.ok(responseDto);
    }
}