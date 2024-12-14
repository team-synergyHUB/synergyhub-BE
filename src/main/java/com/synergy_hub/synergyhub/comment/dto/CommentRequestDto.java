package com.synergy_hub.synergyhub.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentRequestDto {

    @Schema(description = "공지사항 ID", example = "1")
    private Long noticeId;

    @Schema(description = "댓글 내용", example = "This is a comment.")
    private String content;
}
