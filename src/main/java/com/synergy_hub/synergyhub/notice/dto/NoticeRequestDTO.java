package com.synergy_hub.synergyhub.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class NoticeRequestDTO {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "제목을 입력해주세요.")
    private String content;

    private Long memberId;
    private Long teamId;
    private MultipartFile image;

}
