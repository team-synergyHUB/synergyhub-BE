package com.synergy_hub.synergyhub.calendar.repository;

import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long>, CalendarEventRepositoryCustom{

}
