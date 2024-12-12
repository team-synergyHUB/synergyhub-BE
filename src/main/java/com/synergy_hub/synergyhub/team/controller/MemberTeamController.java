package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.config.argumentresolver.AuthenticatedMember;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.team.dto.TeamJoinRequestDTO;
import com.synergy_hub.synergyhub.team.dto.TeamResponseDTO;
import com.synergy_hub.synergyhub.team.dto.UpdateColorRequestDto;
import com.synergy_hub.synergyhub.team.service.MemberTeamService;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-teams")
@Tag(name = "MemberTeam API", description = "팀과 멤버 관계를 관리하는 API") // Swagger 태그
public class MemberTeamController {

    private final MemberTeamService memberTeamService;
    private final TeamService teamService;

    public MemberTeamController(MemberTeamService memberTeamService, TeamService teamService) {
        this.memberTeamService = memberTeamService;
        this.teamService = teamService; // 주입
    }

    // 로그인한 사용자가 속한 팀 목록 조회
    @GetMapping("/member/{memberId}")
    @Operation(summary = "로그인한 사용자의 팀 목록 조회", description = "로그인한 사용자가 속한 팀 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 팀 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByMember(@PathVariable Long memberId) {
        List<TeamResponseDTO> teams = teamService.getTeamsByMember(memberId);
        return ResponseEntity.ok(teams);
    }

    // 특정 팀의 멤버 목록 조회
    @GetMapping("/{teamId}/members")
    @Operation(summary = "팀의 멤버 목록 조회", description = "특정 팀에 속한 멤버 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 멤버 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<MemberResponseDto>> getMembersOfTeam(@PathVariable Long teamId) {
        List<Member> members = memberTeamService.getMembersOfTeam(teamId);
        List<MemberResponseDto> memberDTOs = members.stream()
                .map(MemberResponseDto::new) // Member 엔티티를 DTO로 변환
                .toList();
        return ResponseEntity.ok(memberDTOs);
    }

    @PostMapping("/teams/join")
    @Operation(summary = "팀 참가", description = "초대 코드를 이용하여 팀에 참가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 팀에 참가"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> joinTeam(
            @RequestBody @Valid TeamJoinRequestDTO requestDTO,
            @AuthenticatedMember MemberDetails memberDetails) {

        // 요청 데이터와 인증된 사용자 정보 로그 출력
        System.out.println("1. 요청 수신: " + requestDTO);
        System.out.println("2. 인증된 사용자 정보 - ID: " + memberDetails.getUserId()
                + ", 이메일: " + memberDetails.getUsername()
                + ", 닉네임: " + memberDetails.getNickname());

        // 서비스 호출 (팀 참가 로직 실행)
        memberTeamService.addMemberToTeam(requestDTO.getInviteCode(), memberDetails.getUserId());

        // 성공 응답 반환
        return ResponseEntity.ok("팀에 성공적으로 참가했습니다.");
    }

    @DeleteMapping("/{teamId}/leave")
    @Operation(summary = "팀 나가기", description = "팀에서 나갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 팀에서 나감"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팀 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> leaveTeam(
            @PathVariable Long teamId,
            @AuthenticatedMember MemberDetails memberDetails) {

        // 인증된 사용자 정보 로그 출력
        System.out.println("1. 요청 수신 - 팀 ID: " + teamId);
        System.out.println("2. 인증된 사용자 정보 - ID: " + memberDetails.getUserId()
                + ", 이메일: " + memberDetails.getUsername()
                + ", 닉네임: " + memberDetails.getNickname());

        // 팀에서 멤버 제거 서비스 호출
        memberTeamService.removeMemberFromTeam(teamId, memberDetails.getUserId());

        // 성공 응답 반환
        return ResponseEntity.ok("팀에서 성공적으로 나갔습니다.");
    }

    // 색상 변경
    @PutMapping("/color")
    @Operation(summary = "색상 변경", description = "팀에서 사용자의 색상을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "색상 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팀 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> updateColor(@RequestBody UpdateColorRequestDto updateColorRequestDto){
        memberTeamService.updateColor(updateColorRequestDto.getMemberId(), updateColorRequestDto.getTeamId(), updateColorRequestDto.getNewColor());
        return ResponseEntity.ok("색상 변경 완료");
    }

    // 팀 색상 조회
    @GetMapping("/color")
    @Operation(summary = "팀 색상 조회", description = "특정 팀에서 사용자의 색상을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 색상 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "팀 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<String> getTeamColor(@RequestParam Long memberId, @RequestParam Long teamId) {
        String color = memberTeamService.getTeamColor(memberId, teamId);
        return ResponseEntity.ok(color);
    }

    @GetMapping("/all-color")
    @Operation(summary = "모든 팀 색상 조회", description = "사용자가 속한 모든 팀의 색상을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 색상 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<Long, String>> getAllColor(@RequestParam Long memberId){
        Map<Long, String> color = memberTeamService.getAllTeamColor(memberId);
        return ResponseEntity.ok(color);
    }
}
