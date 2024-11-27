package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.team.service.MemberTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-teams")
@RequiredArgsConstructor
@Tag(name = "MemberTeam API", description = "팀과 멤버 관계를 관리하는 API") // Swagger 태그
public class MemberTeamController {

    private final MemberTeamService memberTeamService;

//    // 팀에 멤버 추가
//    @Operation(summary = "팀에 멤버 추가", description = "특정 팀에 멤버를 추가합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "멤버 추가 성공"),
//            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청")
//    })
//    @PostMapping("/{teamId}/members")
//    public ResponseEntity<Void> addMemberToTeam(
//            @PathVariable Long teamId,
//            @RequestBody MemberRequestDTO memberRequestDTO) {
//        memberTeamService.addMemberToTeam(teamId, memberRequestDTO.toEntity());
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

//    // 팀에서 멤버 제거
//    @Operation(summary = "팀에서 멤버 제거", description = "특정 팀에서 멤버를 제거합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "멤버 제거 성공"),
//            @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없음"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청")
//    })
//    @DeleteMapping("/{teamId}/members/{memberId}")
//    public ResponseEntity<Void> removeMemberFromTeam(
//            @PathVariable Long teamId,
//            @PathVariable Long memberId) {
//        memberTeamService.removeMemberFromTeam(teamId, memberId);
//        return ResponseEntity.noContent().build();
//    }

//    // 특정 팀의 멤버 조회
//    @Operation(summary = "팀 멤버 조회", description = "특정 팀에 속한 모든 멤버를 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "멤버 조회 성공"),
//            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
//    })
//    @GetMapping("/{teamId}/members")
//    public ResponseEntity<List<MemberResponseDTO>> getMembersOfTeam(
//            @PathVariable Long teamId) {
//        List<MemberResponseDTO> members = memberTeamService.getMembersOfTeam(teamId)
//                .stream()
//                .map(MemberResponseDTO::new)
//                .toList();
//        return ResponseEntity.ok(members);
//    }
}
