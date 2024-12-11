package com.synergy_hub.synergyhub.comment.controller;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.dto.CommentUpdateRequestDto;
import com.synergy_hub.synergyhub.comment.service.CommentService;
import com.synergy_hub.synergyhub.global.CommonApiDocs;
import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @CommonApiDocs(summary = "댓글 생성", description = "특정 공지사항에 댓글을 생성합니다.")
    @PostMapping("/notice/{noticeId}")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        // 현재 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 생성
        CommentResponseDto createdComment = commentService.createComment(dto, currentMemberId);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 조회 (특정 공지사항 기준)
    @CommonApiDocs(summary = "댓글 조회", description = "특정 공지사항에 달린 댓글 목록을 조회합니다.")
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByNoticeId(@PathVariable Long noticeId) {
        List<CommentResponseDto> comments = commentService.getCommentsByNoticeId(noticeId);
        return ResponseEntity.ok(comments);
    }

    // 특정 댓글 조회
    @CommonApiDocs(summary = "특정 댓글 조회", description = "특정 댓글의 정보를 조회합니다.")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        // 댓글 조회
        CommentResponseDto comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }


    // 댓글 수정
    @CommonApiDocs(summary = "댓글 수정", description = "특정 댓글의 내용을 수정합니다.")
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
    @CommonApiDocs(summary = "댓글 삭제", description = "특정 댓글을 삭제 처리(Soft Delete)합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> softDeleteComment(@PathVariable Long commentId) {
        // 현재 사용자 ID 가져오기
        Long currentMemberId = MemberController.getAuthenticationMemberId();

        // 댓글 삭제
        commentService.softDeleteComment(commentId, currentMemberId);
        return ResponseEntity.noContent().build();
    }
}
