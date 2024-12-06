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

    private String imageUrl;
    private Long memberId;
    private String memberNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
