package com.synergy_hub.synergyhub.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeUpdateRequestDTO {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

//    @NotBlank(message = "이미지 url 을 입력해주세요.")
    private String imageUrl;

//    private String memberNickname;
//    private Long memberId;
//    private Long teamId;
}
