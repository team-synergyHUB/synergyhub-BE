package com.synergy_hub.synergyhub.notice.controller;

import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.member.service.MemberService;
import com.synergy_hub.synergyhub.notice.dto.NoticeCreateRequestDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeUpdateRequestDTO;
import com.synergy_hub.synergyhub.notice.service.NoticeService;

import com.synergy_hub.synergyhub.team.repository.MemberTeamRepository;
import com.synergy_hub.synergyhub.team.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService; // 현재 사용자 인증 정보 제공
    private final MemberController memberController;
    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final TeamService teamService;



    // 공지사항 생성
    @CommonApiDocs(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @PostMapping("/{teamId}")
    public ResponseEntity<NoticeResponseDTO> createNotice(
            @PathVariable Long teamId,
            @RequestBody @Valid NoticeCreateRequestDTO requestDTO) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        teamService.teamAccessValidator(teamId, currentMember);

        // teamId를 별도로 전달
        NoticeResponseDTO response = noticeService.createNotice(requestDTO, currentMember, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 공지사항 수정
    @CommonApiDocs(summary = "공지사항 수정", description = "기존의 공지사항을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
            @PathVariable Long id,
            @RequestBody @Valid NoticeUpdateRequestDTO requestDTO) {
        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);
        NoticeResponseDTO response = noticeService.updateNotice(id, requestDTO, currentMember);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @CommonApiDocs(summary = "공지사항 삭제", description = "기존의 공지사항을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);
        noticeService.deleteNotice(id, currentMember);
        return ResponseEntity.noContent().build();
    }

    // 특정 공지사항 조회
    @CommonApiDocs(summary = "공지사항 조회", description = "특정 공지사항의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long id) {
        NoticeResponseDTO response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }

    // 특정 팀의 공지사항 조회 (페이지네이션)
    @CommonApiDocs(summary = "특정 팀의 공지사항 조회", description = "특정 팀에 속한 공지사항들을 조회합니다.")
    @GetMapping("/teams/{teamId}/notices")
    public ResponseEntity<Page<NoticeResponseDTO>> getTeamNotices(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);

        Page<NoticeResponseDTO> notices = noticeService.getNoticesByTeam(currentMember, teamId, page, size, sortField, sortDirection);
        return ResponseEntity.ok(notices);
    }

/*
    // 전체 공지사항 조회 (페이지네이션)
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDTO>> getAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NoticeResponseDTO> notices = noticeService.getNoticesByTeam(null, page, size);
        return ResponseEntity.ok(notices);
    }

 */
}
