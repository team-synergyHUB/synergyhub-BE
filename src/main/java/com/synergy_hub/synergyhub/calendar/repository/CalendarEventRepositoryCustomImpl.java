package com.synergy_hub.synergyhub.calendar.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import com.synergy_hub.synergyhub.calendar.entity.QCalendarEvent;
import com.synergy_hub.synergyhub.team.entity.QMemberTeam;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CalendarEventRepositoryCustomImpl implements CalendarEventRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CalendarEventRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CalendarEvent> findEventByTeam(Long teamId) {
        QCalendarEvent calendarEvent = QCalendarEvent.calendarEvent;

        return queryFactory
            .selectFrom(calendarEvent)
            .where(
                calendarEvent.calendar.team.id.eq(teamId),
                calendarEvent.deleteAt.isNull()
            )
            .fetch();
    }

    @Override
    public List<CalendarEvent> findAllEventsForUser(Long memberId) {
        QCalendarEvent calendarEvent = QCalendarEvent.calendarEvent;
        QMemberTeam memberTeam = QMemberTeam.memberTeam;

        return queryFactory
            .selectFrom(calendarEvent)
            .where(
                calendarEvent.calendar.team.in(
                    queryFactory.select(memberTeam.team)
                        .from(memberTeam)
                        .where(memberTeam.member.id.eq(memberId))
                ),
                calendarEvent.deleteAt.isNull()
            )
            .fetch();
    }
//    @Override
//    public List<CalendarEvent> findEventsInDateRange(
//        Long teamId, LocalDateTime startDate, LocalDateTime endDate) {
//        QCalendarEvent calendarEvent = QCalendarEvent.calendarEvent;
//
//        return queryFactory
//            .selectFrom(calendarEvent)
//            .where(
//                calendarEvent.calendar.team.id.eq(teamId),
//                calendarEvent.startDate.goe(startDate),
//                calendarEvent.endDate.loe(endDate),
//                calendarEvent.deleteAt.isNull()
//            )
//            .fetch();
//    }
}
