package com.synergy_hub.synergyhub.comment.mapper;

import com.synergy_hub.synergyhub.comment.dto.CommentRequestDto;
import com.synergy_hub.synergyhub.comment.dto.CommentResponseDto;
import com.synergy_hub.synergyhub.comment.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequestDto dto) {
        Comment comment = new Comment();
        comment.setMemberId(dto.getMemberId());
        comment.setTeamId(dto.getTeamId());
        comment.setContent(dto.getContent());
        return comment;
    }

    public CommentResponseDto toDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setCommentId(comment.getCommentId());

        // Null 체크 후 처리
        if (comment.getNotice() != null) {
            dto.setNoticeId(comment.getNotice().getId()); // 'getNoticeId()'가 아니라 'getId()'로 접근해야 합니다.
        } else {
            dto.setNoticeId(null); // 혹은 적절한 기본값 처리
        }

        dto.setMemberId(comment.getMemberId());
        dto.setTeamId(comment.getTeamId());
        dto.setContent(comment.getContent());
        dto.setIsDeleted(comment.getIsDeleted());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());

        return dto;
    }

}
