package com.synergy_hub.synergyhub.calendar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팀과 1 : 1
    @OneToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 캘린더와 이벤트 1 : N , 캘린더 삭제시 캘린더 이벤트 자동 삭제
    @OneToMany(mappedBy = "caleandar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarEvent> events = new ArrayList<>();

    public void addEvent(CalendarEvent event){
        this.events.add(event);
        event.setCalendar(this);
    }

    public void removeEvent(CalendarEvent event){
        this.events.remove(event);
        event.setCalendar(null);
    }
}
