package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.config.argumentresolver.AuthenticatedMember;
import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.team.dto.*;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final MemberRepository memberRepository;

    // 팀에 라벨 매핑
    @PostMapping("/{teamId}/labels")
    @Operation(summary = "팀에 라벨 매핑", description = "팀에 라벨을 연결합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 라벨이 매핑되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> mapLabelsToTeam(
            @PathVariable Long teamId,
            @RequestBody LabelMappingRequestDTO request) {
        teamService.mapLabelsToTeam(teamId, request.getLabelIds());
        return ResponseEntity.ok("Labels successfully mapped to team.");
    }

    @PostMapping
    @Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "팀이 성공적으로 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<TeamCreateResponseDTO> createTeam(
            @Valid @RequestBody TeamRequestDTO request,
            @AuthenticatedMember MemberDetails memberDetails) {

        // 요청 데이터와 사용자 ID 확인
        System.out.println("1. 요청 수신: " + request);
        System.out.println("2. 인증된 사용자 정보 - ID: " + memberDetails.getUserId()
                + ", 이메일: " + memberDetails.getUsername()
                + ", 닉네임: " + memberDetails.getNickname());

        // 팀 생성
        TeamCreateResponseDTO createdTeam = teamService.createTeamWithMember(request, memberDetails.getUserId());
        System.out.println("3. 생성된 팀: " + createdTeam);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    // 팀 수정
    @PutMapping("/{id}")
    @Operation(summary = "팀 수정", description = "기존 팀의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 정보가 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDTO request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }

    // 팀 나가기
    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "팀 나가기", description = "팀에서 멤버를 제거합니다. 모든 멤버가 나가면 팀은 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "팀 나가기가 성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Void> leaveTeam(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        teamService.leaveTeam(memberId, id);
        return ResponseEntity.noContent().build();
    }

    // 모든 팀 조회 (페이지네이션 포함)
    @GetMapping
    @Operation(summary = "팀 목록 조회", description = "모든 팀 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 목록이 성공적으로 반환되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Page<TeamResponseDTO>> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getAllTeams(page, size));
    }

    // 멤버가 속한 팀 조회
    @GetMapping("/member")
    @Operation(summary = "멤버의 팀 목록 조회", description = "로그인한 멤버가 속한 팀 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버의 팀 목록이 성공적으로 반환되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByLoggedInMember(
            @AuthenticatedMember MemberDetails memberDetails) {

        // 인증된 사용자 정보 로그 출력
        System.out.println("요청한 사용자 정보 - ID: " + memberDetails.getUserId()
                + ", 이메일: " + memberDetails.getUsername()
                + ", 닉네임: " + memberDetails.getNickname());

        // 사용자 ID에 속한 팀 조회
        List<TeamResponseDTO> teams = teamService.getTeamsByMember(memberDetails.getUserId());

        // 조회 결과 반환
        return ResponseEntity.ok(teams);
    }


    // 초대 코드 조회 API
    @GetMapping("/{teamId}/invite-code")
    @Operation(summary = "팀 초대 코드 조회", description = "팀의 초대 코드를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "초대 코드가 성공적으로 반환되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<InviteCodeResponseDTO> getInviteCode(@PathVariable Long teamId) {
        String inviteCode = teamService.getInviteCodeByTeamId(teamId);
        InviteCodeResponseDTO responseDto = new InviteCodeResponseDTO(inviteCode);
        return ResponseEntity.ok(responseDto);
    }

    // 팀 검증 로직
    @GetMapping("{teamId}/validate")
    @Operation(summary = "팀 접근 검증", description = "팀 접근 권한을 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 접근 검증이 성공적으로 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 부족"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Boolean> teamValidate(@PathVariable Long teamId) {
        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);

        if (currentMember == null) {
            return ResponseEntity.badRequest().body(false); // 멤버가 없으면 접근 불가
        }

        boolean isValid = teamService.teamAccessValidator(teamId, currentMember);
        return ResponseEntity.ok(isValid); // 검증 결과 반환
    }

}