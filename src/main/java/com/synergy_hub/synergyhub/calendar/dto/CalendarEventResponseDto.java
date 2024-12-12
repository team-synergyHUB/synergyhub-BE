package com.synergy_hub.synergyhub.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CalendarEventResponseDto {
    @Schema(description = "캘린더 이벤트 ID", example = "1")
    private Long id;

    @Schema(description = "캘린더 이벤트 제목", example = "팀 회의")
    private String title;

    @Schema(description = "캘린더 이벤트 시작 시간", example = "2024-12-10T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "캘린더 이벤트 종료 시간", example = "2024-12-10T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "삭제된 시간 (삭제되지 않은 경우 null)", example = "null")
    private LocalDateTime deletedAt;

    @Schema(description = "캘린더 이벤트 색상", example = "#FF5733")
    private String color;

    @Schema(description = "팀 이름", example = "1팀")
    private String teamName;
}
