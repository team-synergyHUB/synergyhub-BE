package com.synergy_hub.synergyhub.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeUpdateRequestDTO {

    @Schema(description = "공지사항 제목", example = "팀 회의 일정 변경", required = true)
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "공지사항 내용", example = "다음 주 화요일 오후 5시로 팀 회의 시간이 변경되었습니다.", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

//    @NotBlank(message = "이미지 url 을 입력해주세요.")
    @Schema(description = "이미지 파일의 URL (선택 사항)", example = "https://example.com/images/notice_update.png", required = false)
    private String imageUrl;

//    private String memberNickname;
//    private Long memberId;
//    private Long teamId;
}
