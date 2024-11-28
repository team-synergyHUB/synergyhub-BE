package com.synergy_hub.synergyhub.calendar.entity;

import com.synergy_hub.synergyhub.team.entity.Team;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팀과 1 : 1
    @OneToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 캘린더와 이벤트 1 : N
    @OneToMany(mappedBy = "calendar")
    private List<CalendarEvent> events = new ArrayList<>();


    public void addEvent(CalendarEvent event){
        this.events.add(event);
        event.assignCalendar(this);
    }

    //캘린더에서 이벤트 제거 softdelete
    public void removeEvent(CalendarEvent event){
        event.markAsDeleted();
    }
}
