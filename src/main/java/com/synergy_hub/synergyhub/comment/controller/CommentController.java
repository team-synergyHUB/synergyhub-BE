package com.synergy_hub.synergyhub.comment.controller;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.dto.CommentUpdateRequestDto;
import com.synergy_hub.synergyhub.comment.service.CommentService;
import com.synergy_hub.synergyhub.member.controller.MemberController;
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
    @PostMapping("/notice/{noticeId}")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        // 현재 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 생성
        CommentResponseDto createdComment = commentService.createComment(dto, currentMemberId);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 조회 (특정 공지사항 기준)
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNoticeId(@PathVariable Long noticeId) {
        List<CommentResponseDto> comments = commentService.getCommentsByNoticeId(noticeId);
        return ResponseEntity.ok(comments);
    }

    // 특정 댓글 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        // 댓글 조회
        CommentResponseDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }


    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto updateRequestDto) {
        // 현재 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 수정
        CommentResponseDto updatedComment = commentService.updateComment(commentId, updateRequestDto.getContent(), currentMemberId);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제 (Soft Delete)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> softDeleteComment(@PathVariable Long commentId) {
        // 현재 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 삭제
        commentService.softDeleteComment(commentId, currentMemberId);
        return ResponseEntity.noContent().build();
    }
}
