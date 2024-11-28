package com.synergy_hub.synergyhub.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamRequestDTO {
    @NotBlank(message = "팀 이름은 필수 입력 항목입니다.")
    private String name; // 팀 이름

//    @NotBlank(message = "초대 코드는 필수 입력 항목입니다.")
//    private String inviteCode; // 초대 코드
//
//    @NotBlank(message = "초대 비밀번호는 필수 입력 항목입니다.")
//    private String inviteSecret; // 초대 비밀번호

    private Long labelId; // 라벨 ID (선택 항목)

    private Boolean isDeleted = false; // 기본값 설정
}
