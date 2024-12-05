package com.synergy_hub.synergyhub.notice.dto;

import com.synergy_hub.synergyhub.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeResponseDTO {

    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String memberNickname;
    private Long teamId;
    private String teamName;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static NoticeResponseDTO fromEntity(Notice notice) {
        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getMember().getId(),
                notice.getMember().getNickname(),
                notice.getTeam().getId(),
                notice.getTeam().getName(),
                notice.getImageUrl(),
                notice.getCreatedAt(),
                notice.getUpdatedAt(),
                notice.getDeletedAt()
        );
    }
}
