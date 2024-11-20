package com.synergy_hub.synergyhub.calendar.repository;

import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

}
