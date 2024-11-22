package com.synergy_hub.synergyhub.calendar.dto;


import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CalendarEventRequestDTO {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean allDay;

}
