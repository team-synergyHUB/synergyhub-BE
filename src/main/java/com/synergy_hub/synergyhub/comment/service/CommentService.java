package com.synergy_hub.synergyhub.comment.service;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.entity.Comment;
import com.synergy_hub.synergyhub.notice.entity.Notice;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.comment.mapper.CommentMapper;
import com.synergy_hub.synergyhub.comment.repository.CommentRepository;
import com.synergy_hub.synergyhub.notice.repository.NoticeRepository;
import com.synergy_hub.synergyhub.team.entity.Team;
import com.synergy_hub.synergyhub.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final NoticeRepository noticeRepository;
    private final CommentMapper commentMapper;

    // 댓글 생성
    public CommentResponseDto createComment(CommentRequestDto dto, Long currentMemberId) {
        // 공지사항 조회
        Notice notice = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 공지사항에서 Team 가져오기
        Team team = notice.getTeam();

        // 댓글 엔티티 생성
        Comment comment = Comment.builder()
                .noticeId(dto.getNoticeId())
                .memberId(currentMemberId)
                .teamId(team.getId())
                .content(dto.getContent())
                .isDeleted(false)
                .build();

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }


    // 댓글 조회 (특정 공지사항)
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByNoticeId(Long noticeId) {
        return commentRepository.findByNoticeIdAndIsDeletedFalse(noticeId)
            .stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toList());
    }

    // 특정 댓글 조회
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 댓글을 DTO로 변환하여 반환
        return commentMapper.toDto(comment);
    }


    // 댓글 수정
    public CommentResponseDto updateComment(Long commentId, String content, Long currentMemberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (comment.getIsDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_DELETED);
        }

        // 작성자 검증
        if (!comment.getMemberId().equals(currentMemberId)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    // 댓글 삭제 (Soft Delete)
    public void softDeleteComment(Long commentId, Long currentMemberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (comment.getIsDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_DELETED);
        }

        // 작성자 검증
        if (!comment.getMemberId().equals(currentMemberId)) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }
}

