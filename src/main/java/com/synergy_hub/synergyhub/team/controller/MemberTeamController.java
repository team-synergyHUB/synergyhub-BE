package com.synergy_hub.synergyhub.team.controller;

import com.synergy_hub.synergyhub.member.entity.Member;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member-teams")
//@RequiredArgsConstructor
@Tag(name = "MemberTeam API", description = "팀과 멤버 관계를 관리하는 API") // Swagger 태그
public class MemberTeamController {

    private final MemberTeamService memberTeamService;
    private final TeamService teamService;

    public MemberTeamController(MemberTeamService memberTeamService, TeamService teamService) {
        this.memberTeamService = memberTeamService;
        this.teamService = teamService; // 주입
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
     * 팀에 멤버 추가
     * @param teamId 팀 ID
     * @param member 멤버 정보 (RequestBody를 통해 전달)
     */
    @PostMapping("/{teamId}/add")
    public ResponseEntity<Void> addMemberToTeam(@PathVariable Long teamId, @RequestBody Member member) {
        memberTeamService.addMemberToTeam(teamId, member);
        return ResponseEntity.ok().build();
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
