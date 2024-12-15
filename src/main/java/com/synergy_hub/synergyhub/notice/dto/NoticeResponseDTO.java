package com.synergy_hub.synergyhub.notice.dto;

import com.synergy_hub.synergyhub.notice.entity.Notice;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeResponseDTO {

    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "공지사항 제목", example = "팀 회의 공지")
    private String title;

    @Schema(description = "공지사항 내용", example = "다음 주 화요일 오후 3시에 팀 회의가 있습니다.")
    private String content;

    @Schema(description = "이미지 파일의 URL", example = "https://example.com/images/notice1.png")
    private String imageUrl;

    @Schema(description = "작성자 ID", example = "1")
    private Long memberId;

    @Schema(description = "작성자 닉네임", example = "team_leader")
    private String memberNickname;

    @Schema(description = "공지사항 생성일", example = "2024-12-10T15:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "공지사항 수정일", example = "2024-12-11T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "공지사항 삭제일 (삭제되지 않은 경우 null)", example = "null")
    private LocalDateTime deletedAt;

    public static NoticeResponseDTO fromEntity(Notice notice) {
        return new NoticeResponseDTO(
            notice.getId(),                           // Notice의 ID
            notice.getTitle(),                        // 제목
            notice.getContent(),                      // 내용
            notice.getImageUrl(),
            notice.getMember().getId(),               // 작성자 ID
            notice.getMember().getNickname(),         // 작성자 닉네임
            notice.getCreatedAt(),                    // 작성일
            notice.getUpdatedAt(),                    // 수정일
            notice.getDeletedAt()                     // 삭제일
        );
    }
}
