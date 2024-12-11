package com.synergy_hub.synergyhub.calendar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 캘린더와 캘린더이벤트는 N : 1
    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime deleteAt;

    // 삭제 상태를 설정 ( 현재 시간 기록 )
    public void markAsDeleted() {
        this.deleteAt = LocalDateTime.now();
    }
    // deleteAt 값이 null 이면 삭제되지 않은 상태, 값이 있으면 삭제된 상태
    public boolean isDelete() {
        return this.deleteAt !=null;
    }

    public void updateEventDetails(String title, LocalDateTime startDate, LocalDateTime endDate){
        this.title = title;
        this.startDate =startDate;
        this.endDate = endDate;
    }

    // 연관관계 편의 메서드 (Calendar 객체 설정)
    public void assignCalendar(Calendar calendar) {
        this.calendar = calendar;
    }




}
