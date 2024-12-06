package com.synergy_hub.synergyhub.notice.controller;

import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import com.synergy_hub.synergyhub.member.service.MemberService;
import com.synergy_hub.synergyhub.notice.dto.NoticeCreateRequestDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeResponseDTO;
import com.synergy_hub.synergyhub.notice.dto.NoticeUpdateRequestDTO;
import com.synergy_hub.synergyhub.notice.service.NoticeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService; // 현재 사용자 인증 정보 제공
    private final MemberController memberController;
    private final MemberRepository memberRepository;

    // 공지사항 생성
    @PostMapping
    public ResponseEntity<NoticeResponseDTO> createNotice(@RequestBody @Valid NoticeCreateRequestDTO requestDTO) {
        Long currentMemberId = memberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);
        NoticeResponseDTO response = noticeService.createNotice(requestDTO, currentMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 공지사항 수정
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
            @PathVariable Long id,
            @RequestBody @Valid NoticeUpdateRequestDTO requestDTO) {
        Long currentMemberId = memberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);
        NoticeResponseDTO response = noticeService.updateNotice(id, requestDTO, currentMember);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        Long currentMemberId = memberController.getAuthenticationMemberId(); // 인증된 현재 사용자 가져오기
        Member currentMember = memberRepository.findById(currentMemberId).orElse(null);
        noticeService.deleteNotice(id, currentMember);
        return ResponseEntity.noContent().build();
    }

    // 특정 공지사항 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDTO> getNotice(@PathVariable Long id) {
        NoticeResponseDTO response = noticeService.getNotice(id);
        return ResponseEntity.ok(response);
    }

    // 특정 팀의 공지사항 조회 (페이지네이션)
    @GetMapping("/teams/{teamId}/notices")
    public ResponseEntity<Page<NoticeResponseDTO>> getTeamNotices(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        Page<NoticeResponseDTO> notices = noticeService.getNoticesByTeam(teamId, page, size, sortField, sortDirection);
        return ResponseEntity.ok(notices);
    }

/* TODO
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
