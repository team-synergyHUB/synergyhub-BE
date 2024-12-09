package com.synergy_hub.synergyhub.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeCreateRequestDTO {

    @Schema(description = "공지사항 제목" ,example = "팀 회의 공지", required = true )
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "공지사항 내용", example = "다음 주 화요일 오후 3시에 팀 회의가 있습니다.", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Schema(description = "이미지 파일의 URL", example = "https://example.com/images/notice1.png", required = false)
    private String imageUrl;

//    private Long teamId;
}
