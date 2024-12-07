package com.synergy_hub.synergyhub.comment.controller;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.service.CommentService;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto, @RequestParam("team") Long teamId) {
        // 현재 인증된 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 생성 시 회원 정보와 팀 ID 전달
        CommentResponseDto createdComment = commentService.createComment(dto, currentMemberId, teamId);
        return ResponseEntity.ok(createdComment);
    }


    // 댓글 조회 (특정 공지사항 기준)
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNoticeId(@PathVariable Long noticeId) {
        List<CommentResponseDto> comments = commentService.getCommentsByNoticeId(noticeId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
        @PathVariable Long commentId,
        @RequestParam String content) {
        CommentResponseDto updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제 (Soft Delete)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> softDeleteComment(@PathVariable Long commentId) {
        commentService.softDeleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
