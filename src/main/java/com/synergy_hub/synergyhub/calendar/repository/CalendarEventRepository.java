package com.synergy_hub.synergyhub.calendar.repository;

import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // 삭제되지 않은 해당 팀의 일정 조회
    @Query("SELECT e FROM CalendarEvent e WHERE e.calendar.team.id = :teamId AND e.deleteAt IS NULL")
    List<CalendarEvent> findByTeamId(@Param("teamId") Long teamId);

    // 삭제되지 않은 사용자의 모든 팀 일정 조회
    @Query("SELECT e FROM CalendarEvent e WHERE e.calendar.team IN " +
        "(SELECT tm.team FROM MemberTeam tm WHERE tm.member.id = :memberId) AND e.deleteAt IS NULL")
    List<CalendarEvent> findAllEventsForUser(@Param("memberId") Long memberId);

//    // 삭제되지 않은 날짜별 일정 조회, 부가기능으로 생각중
//    @Query("SELECT e FROM CalendarEvent e WHERE e.startDate <= :endDate AND e.endDate >= :startDate AND e.deleteAt IS NULL")
//    List<CalendarEvent> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//

}
