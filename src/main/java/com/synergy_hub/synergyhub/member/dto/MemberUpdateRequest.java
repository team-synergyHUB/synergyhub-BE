package com.synergy_hub.synergyhub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberUpdateRequest {

    @Schema(description = "변경할 닉네임", example = "new_nickname")
    private String nickname;

    @Schema(description = "프로필 이미지 변경 URL", example = "https://example.com/profile/new_image.png")
    private String profileImageUrl;
}
