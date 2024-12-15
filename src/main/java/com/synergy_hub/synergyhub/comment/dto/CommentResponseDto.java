package com.synergy_hub.synergyhub.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {

    @Schema(description = "댓글 ID", example = "501")
    private Long commentId;

    @Schema(description = "공지사항 ID", example = "1")
    private Long noticeId;

    @Schema(description = "멤버 ID", example = "201")
    private Long memberId;

    private String nickname;

    @Schema(description = "팀 ID", example = "301")
    private Long teamId;

    @Schema(description = "댓글 내용", example = "This is a comment.")
    private String content;

    @Schema(description = "삭제 여부", example = "false")
    private Boolean isDeleted;

    @Schema(description = "댓글 생성일", example = "2024-12-10T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수정일", example = "2024-12-11T14:20:00")
    private LocalDateTime updatedAt;
}
