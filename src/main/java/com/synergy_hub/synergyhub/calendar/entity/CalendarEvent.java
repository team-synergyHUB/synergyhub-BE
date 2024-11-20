package com.synergy_hub.synergyhub.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 캘린더와 캘린더이벤트는 N : 1
    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar clendar;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean allDay;

    private boolean isDeleted;

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
