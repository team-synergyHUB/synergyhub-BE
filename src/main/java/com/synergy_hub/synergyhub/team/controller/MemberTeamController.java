package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.config.argumentresolver.AuthenticatedMember;
import com.synergy_hub.synergyhub.member.controller.MemberController;
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

    // 로그인한 사용자가 속한 팀 목록 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsByMember(@PathVariable Long memberId) {
        List<TeamResponseDTO> teams = teamService.getTeamsByMember(memberId);
        return ResponseEntity.ok(teams);
    }

    // 특정 팀의 멤버 목록 조회
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<MemberResponseDto>> getMembersOfTeam(@PathVariable Long teamId) {
        List<Member> members = memberTeamService.getMembersOfTeam(teamId);
        List<MemberResponseDto> memberDTOs = members.stream()
                .map(MemberResponseDto::new) // Member 엔티티를 DTO로 변환
                .toList();
        return ResponseEntity.ok(memberDTOs);
    }

//    @PostMapping("/teams/join")
//    public ResponseEntity<String> joinTeam(@RequestBody @Valid TeamJoinRequestDTO requestDTO,
//                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
//        // 1. 요청 데이터 확인 (디버깅 로그)
//        System.out.println("1. 요청 수신: " + requestDTO);
//
//        // 2. Authorization 헤더 유효성 검사
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
//        }
//
//        // 3. Bearer 토큰 추출
//        String token = authorizationHeader.replace("Bearer ", "").trim();
//        System.out.println("2. 추출된 토큰: " + token); // 토큰 확인 로그
//
//        // 4. 토큰으로 사용자 ID 추출
//        Long memberId = jwtTokenProvider.getUserId(token);
//        System.out.println("3. 추출된 사용자 ID: " + memberId); // 사용자 ID 확인 로그
//
//        // 5. 서비스 호출 (팀 참가 로직 실행)
//        memberTeamService.addMemberToTeam(requestDTO.getInviteCode(), memberId);
//
//        // 6. 성공 응답 반환
//        return ResponseEntity.ok("팀에 성공적으로 참가했습니다.");
//    }

    @PostMapping("/teams/join")
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


//    @DeleteMapping("/{teamId}/leave")
//    public ResponseEntity<String> leaveTeam(
//            @PathVariable Long teamId,
//            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
//        // 1. Authorization 헤더 유효성 검사
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
//        }
//
//        // 2. Bearer 토큰 추출
//        String token = authorizationHeader.replace("Bearer ", "").trim();
//        System.out.println("2. 추출된 토큰: " + token); // 토큰 확인 로그
//
//        // 3. 토큰으로 사용자 ID 추출
//        Long memberId = jwtTokenProvider.getUserId(token);
//        System.out.println("3. 추출된 사용자 ID: " + memberId); // 사용자 ID 확인 로그
//
//        // 4. 팀에서 멤버 제거 서비스 호출
//        memberTeamService.removeMemberFromTeam(teamId, memberId);
//
//        // 5. 성공 응답 반환
//        return ResponseEntity.ok("팀에서 성공적으로 나갔습니다.");
//    }

    @DeleteMapping("/{teamId}/leave")
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


    @PutMapping("/color")
    public ResponseEntity<String> updateColor(@RequestBody UpdateColorRequestDto updateColorRequestDto) {
        Long currentMemberId = MemberController.getAuthenticationMemberId();
        memberTeamService.updateColor(currentMemberId, updateColorRequestDto.getTeamId(), updateColorRequestDto.getNewColor());
        return ResponseEntity.ok("색상 변경 완료");
    }


    // 팀 색상 조회
    @GetMapping("/color")
    public ResponseEntity<String> getTeamColor(@RequestParam Long teamId) {

        Long currentMemberId = MemberController.getAuthenticationMemberId();

        String color = memberTeamService.getTeamColor(currentMemberId, teamId);
        return ResponseEntity.ok(color);
    }

    @GetMapping("/all-color")
    public ResponseEntity<Map<Long, String>> getAllColor(){

        Long currentMemberId = MemberController.getAuthenticationMemberId();

        Map<Long, String> color = memberTeamService.getAllTeamColor(currentMemberId);
        return ResponseEntity.ok(color);
    }
}
