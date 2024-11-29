package com.synergy_hub.synergyhub.calendar.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CalendarEventResponseDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean allDay;
    private LocalDateTime deletedAt;
    private String color;

}
