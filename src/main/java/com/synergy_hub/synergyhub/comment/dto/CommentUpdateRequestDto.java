package com.synergy_hub.synergyhub.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentUpdateRequestDto {

    @Schema(description = "수정할 댓글 내용", example = "Updated comment content.")
    private String content;
}
