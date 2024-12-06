package com.synergy_hub.synergyhub.member.controller;

import com.synergy_hub.synergyhub.auth.oauth.dto.CustomOauth2User;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.dto.MemberUpdateRequest;
import com.synergy_hub.synergyhub.member.dto.TeamMemberResponseDto;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.exception.MemberNotAuthenticatedException;
import com.synergy_hub.synergyhub.member.service.MemberService;
import jakarta.validation.constraints.Null;
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
public class MemberController {

    private final MemberService memberService;

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
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponseDto>>> getAllMembers() {
        List<MemberResponseDto> members = memberService.findAllMembers();

        return ApiResponseBuilder.success("Get All Members successfully", members,
            HttpStatus.OK);
    }

    //내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponseDto>> getMyInfo() {
//        String email = getAuthenticationEmail();
//        MemberResponseDto memberResponseDto = memberService.findByEmail(email);

        Long memberId = getAuthenticationMemberId();
        MemberResponseDto memberResponseDto = memberService.findById(memberId);

        return ApiResponseBuilder.success("Get My Info successfully", memberResponseDto,
            HttpStatus.OK);
    }

    //특정 팀에 속한 회원 목록 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Page<TeamMemberResponseDto>>> getTeamMember(
        @PathVariable Long teamId, Pageable pageable) {
        Page<TeamMemberResponseDto> teamMembers = memberService.findAllByTeamPaging(teamId,
            pageable);

        return ApiResponseBuilder.success("Get Team Members successfully", teamMembers,
            HttpStatus.OK);

    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateMyInfo(
        @RequestBody MemberUpdateRequest request) {
        String email = getAuthenticationEmail();

        memberService.updateMemberInfo(email, request);

        return ApiResponseBuilder.success("Update MyInfo successfully", null,
            HttpStatus.OK);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount() {
        String email = getAuthenticationEmail();

        memberService.deleteMember(email);

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
