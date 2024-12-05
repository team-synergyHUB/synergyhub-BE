package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.member.entity.Member;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-teams")
//@RequiredArgsConstructor
@Tag(name = "MemberTeam API", description = "팀과 멤버 관계를 관리하는 API") // Swagger 태그
public class MemberTeamController {

    private final MemberTeamService memberTeamService;
    private final TeamService teamService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberTeamController(MemberTeamService memberTeamService, TeamService teamService,
                                JwtTokenProvider jwtTokenProvider) {
        this.memberTeamService = memberTeamService;
        this.teamService = teamService; // 주입
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 로그인한 사용자가 속한 팀 목록 조회
     * @param memberId 로그인한 사용자의 ID
     * @return 사용자가 속한 팀 목록
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByMember(@PathVariable Long memberId) {
        List<TeamResponseDTO> teams = teamService.getTeamsByMember(memberId);
        return ResponseEntity.ok(teams);
    }

    /**
     * 특정 팀의 멤버 목록 조회
     * @param teamId 팀 ID
     * @return 팀에 속한 멤버 목록
     */
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<Member>> getMembersOfTeam(@PathVariable Long teamId) {
        List<Member> members = memberTeamService.getMembersOfTeam(teamId);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/teams/join")
    public ResponseEntity<String> joinTeam(@RequestBody @Valid TeamJoinRequestDTO requestDTO,
                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // 1. 요청 데이터 확인 (디버깅 로그)
        System.out.println("1. 요청 수신: " + requestDTO);

        // 2. Authorization 헤더 유효성 검사
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }

        // 3. Bearer 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "").trim();
        System.out.println("2. 추출된 토큰: " + token); // 토큰 확인 로그

        // 4. 토큰으로 사용자 ID 추출
        Long memberId = jwtTokenProvider.getUserId(token);
        System.out.println("3. 추출된 사용자 ID: " + memberId); // 사용자 ID 확인 로그

        // 5. 서비스 호출 (팀 참가 로직 실행)
        memberTeamService.addMemberToTeam(requestDTO.getInviteCode(), memberId);

        // 6. 성공 응답 반환
        return ResponseEntity.ok("팀에 성공적으로 참가했습니다.");
    }

    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<String> leaveTeam(
            @PathVariable Long teamId,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        // 1. Authorization 헤더 유효성 검사
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }

        // 2. Bearer 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "").trim();
        System.out.println("2. 추출된 토큰: " + token); // 토큰 확인 로그

        // 3. 토큰으로 사용자 ID 추출
        Long memberId = jwtTokenProvider.getUserId(token);
        System.out.println("3. 추출된 사용자 ID: " + memberId); // 사용자 ID 확인 로그

        // 4. 팀에서 멤버 제거 서비스 호출
        memberTeamService.removeMemberFromTeam(teamId, memberId);

        // 5. 성공 응답 반환
        return ResponseEntity.ok("팀에서 성공적으로 나갔습니다.");
    }

    // 색상 변경
    @PutMapping("/color")
    public ResponseEntity<String> updateColor(@RequestBody UpdateColorRequestDto updateColorRequestDto){
        memberTeamService.updateColor(updateColorRequestDto.getMemberId(), updateColorRequestDto.getTeamId(), updateColorRequestDto.getNewColor());
        return ResponseEntity.ok("색상 변경 완료");
    }

    // 팀 색상 조회
    @GetMapping("/color")
    public ResponseEntity<String> getTeamColor(@RequestParam Long memberId, @RequestParam Long teamId) {
        String color = memberTeamService.getTeamColor(memberId, teamId);
        return ResponseEntity.ok(color);
    }

    @GetMapping("/all-color")
    public ResponseEntity<Map<Long, String>> getAllColor(@RequestParam Long memberId){
        Map<Long, String> color = memberTeamService.getAllTeamColor(memberId);
        return ResponseEntity.ok(color);
    }
}
