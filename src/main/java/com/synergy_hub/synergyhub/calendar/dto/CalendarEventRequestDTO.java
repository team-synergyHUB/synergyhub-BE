package com.synergy_hub.synergyhub.calendar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CalendarEventRequestDTO {

    @Schema(description = "캘린더 이벤트 제목", example = "팀 회의")
    private String title;

    @Schema(description = "캘린더 이벤트 시작 시간", example = "2024-12-10T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "캘린더 이벤트 종료 시간", example = "2024-12-10T12:00:00")
    private LocalDateTime endDate;

}
