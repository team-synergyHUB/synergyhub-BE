package com.synergy_hub.synergyhub.calendar.repository;

import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepositoryCustom {

    // 삭제되지 않은 팀 일정 조회
    List<CalendarEvent> findEventByTeam(Long teamId);

    // 삭제되지 않은 사용자의 모든 팀 일정 조회
    List<CalendarEvent> findAllEventsForUser(Long memberId);

    // 삭제되지 않은 날짜별 일정 조회, 부가기능으로 생각중
//    List<CalendarEvent> findEventsInDeateRange(
//    Long teamId, LocalDateTime startDate, LocalDateTime endDate);

}
