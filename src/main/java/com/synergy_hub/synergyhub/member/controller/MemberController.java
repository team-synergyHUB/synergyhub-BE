package com.synergy_hub.synergyhub.member.controller;

import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.dto.MemberUpdateRequest;
import com.synergy_hub.synergyhub.member.dto.TeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.exception.MemberNotAuthenticatedException;
import com.synergy_hub.synergyhub.config.argumentresolver.AuthenticatedMember;
import com.synergy_hub.synergyhub.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Slf4j
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @CommonApiDocs(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signUp(
        @RequestBody MemberAddRequest request) {
        Long savedMemberId = memberService.save(request);

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", savedMemberId);

        return ApiResponseBuilder.success("Member created successfully", payLoad,
            HttpStatus.CREATED);

    }

    //모든 회원 조회
    @CommonApiDocs(summary = "모든 회원 조회", description = "모든 회원의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponseDto>>> getAllMembers() {
        List<MemberResponseDto> members = memberService.findAllMembers();

        return ApiResponseBuilder.success("Get All Members successfully", members,
            HttpStatus.OK);
    }

    //내 정보 조회
    @CommonApiDocs(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo(
        @AuthenticatedMember MemberDetails memberDetails) {

        Long memberId = memberDetails.getUserId();
        MemberResponseDto memberResponseDto = memberService.findById(memberId);

        return ApiResponseBuilder.success("Get My Info successfully", memberResponseDto,
            HttpStatus.OK);
    }

    //특정 팀에 속한 회원 목록 조회
    @CommonApiDocs(summary = "특정 팀 회원 목록 조회", description = "특정 팀에 속한 회원들의 목록을 조회합니다.")
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Page<TeamMemberResponseDto>>> getTeamMember(
        @PathVariable Long teamId, Pageable pageable) {
        Page<TeamMemberResponseDto> teamMembers = memberService.findAllByTeamPaging(teamId,
            pageable);

        return ApiResponseBuilder.success("Get Team Members successfully", teamMembers,
            HttpStatus.OK);

    }
    @CommonApiDocs(summary = "내 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(
        @RequestBody MemberUpdateRequest request, @AuthenticatedMember MemberDetails memberDetails) {

        memberService.updateMemberInfo(memberDetails.getUserId(), request);

        return ApiResponseBuilder.success("Update MyInfo successfully", null,
            HttpStatus.OK);
    }

    @CommonApiDocs(summary = "계정 삭제", description = "현재 로그인한 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(
        @AuthenticatedMember MemberDetails memberDetails) {

        memberService.deleteMember(memberDetails.getUserId());

        return ApiResponseBuilder.success("Delete Account successfully", null,
            HttpStatus.NO_CONTENT);

    }


    private String getAuthenticationEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            throw new MemberNotAuthenticatedException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        throw new MemberNotAuthenticatedException(ErrorCode.USER_NOT_AUTHENTICATED);
    }

    public static Long getAuthenticationMemberId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            throw new MemberNotAuthenticatedException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        return ((MemberDetails) principal).getUserId();
    }



}
