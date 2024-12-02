package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.config.global.SwaggerDocumentation;
import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.team.dto.LabelMappingRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamCreateResponseDTO;
import com.synergy_hub.synergyhub.team.dto.TeamRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/teams")
//@RequiredArgsConstructor
//@Tag(name = "Team API", description = "팀 관련 API") // Swagger 태그
//@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 도메인
//public class TeamController {
//
//    private final TeamService teamService;
//
//    @PostMapping("/{teamId}/labels")
//    public ResponseEntity<String> mapLabelsToTeam(
//            @PathVariable Long teamId, // URL 경로에서 팀 ID 추출
//            @RequestBody LabelMappingRequestDTO request // 요청 본문에서 라벨 ID 리스트 추출
//    ) {
//        teamService.mapLabelsToTeam(teamId, request.getLabelIds());
//        return ResponseEntity.ok("Labels successfully mapped to team.");
//    }
//
//    @PostMapping
//    public ResponseEntity<TeamCreateResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO request) {
//        TeamCreateResponseDTO response = teamService.createTeam(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + ex.getMessage());
//    }
//
//    @Operation(summary = "팀 수정", description = "기존 팀의 정보를 수정합니다.")
//    @SwaggerDocumentation.UpdateTeamResponses // 공통 응답 사용
//    @PutMapping("/{id}")
//    public ResponseEntity<TeamResponseDTO> updateTeam(
//            @PathVariable Long id,
//            @Valid @RequestBody TeamRequestDTO request) {
//        TeamResponseDTO response = teamService.updateTeam(id, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @Operation(summary = "팀 나가기", description = "팀에서 멤버를 제거합니다. 모든 멤버가 나가면 팀은 삭제됩니다.")
//    @SwaggerDocumentation.LeaveTeamResponses // 공통 응답 사용
//    @DeleteMapping("/{id}/members/{memberId}")
//    public ResponseEntity<Void> leaveTeam(
//            @PathVariable Long id,
//            @PathVariable Long memberId) {
//        teamService.leaveTeam(memberId, id);
//        return ResponseEntity.noContent().build();
//    }
//
//
////
////    @Operation(summary = "팀 목록 조회", description = "모든 팀 목록을 조회합니다.")
////    @SwaggerDocumentation.CommonResponses // 공통 응답 사용
////    @GetMapping
////    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
////        List<TeamResponseDTO> teams = teamService.getAllTeams();
////        return ResponseEntity.ok(teams);
////    }
//
//    // 팀 조회 API with 페이지네이션
//    @GetMapping
//    public ResponseEntity<Page<TeamResponseDTO>> getAllTeams(
//            @RequestParam(defaultValue = "0") int page, // 페이지 번호 (기본값 0)
//            @RequestParam(defaultValue = "10") int size // 한 페이지 데이터 개수 (기본값 10)
//    ) {
//        Page<TeamResponseDTO> teams = teamService.getAllTeams(page, size); // 서비스 호출
//        return ResponseEntity.ok(teams); // Page 객체 반환
//    }
//
//
//}

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Team API", description = "팀 관련 API") // Swagger 태그
@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 도메인
public class TeamController {

    private final TeamService teamService;

    // 팀에 라벨 매핑
    @CommonApiDocs(summary = "팀에 라벨 매핑", description = "팀에 라벨을 연결합니다.")
    @PostMapping("/{teamId}/labels")
    public ResponseEntity<String> mapLabelsToTeam(
            @PathVariable Long teamId,
            @RequestBody LabelMappingRequestDTO request) {
        teamService.mapLabelsToTeam(teamId, request.getLabelIds());
        return ResponseEntity.ok("Labels successfully mapped to team.");
    }

    // 팀 생성
    @CommonApiDocs(summary = "팀 생성", description = "새로운 팀을 생성합니다.")
    @PostMapping
    public ResponseEntity<TeamCreateResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request));
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
}