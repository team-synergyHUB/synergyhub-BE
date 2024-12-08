package com.synergy_hub.synergyhub.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long commentId;
    private Long noticeId;
    private Long memberId;
    private Long teamId;
    private String content;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
