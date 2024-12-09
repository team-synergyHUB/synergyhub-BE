package com.synergy_hub.synergyhub.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private Long noticeId;
    private String content;
}