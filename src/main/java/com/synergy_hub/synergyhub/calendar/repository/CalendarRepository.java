package com.synergy_hub.synergyhub.calendar.repository;

import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
