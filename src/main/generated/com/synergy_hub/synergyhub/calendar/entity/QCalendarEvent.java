package com.synergy_hub.synergyhub.calendar.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCalendarEvent is a Querydsl query type for CalendarEvent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalendarEvent extends EntityPathBase<CalendarEvent> {

    private static final long serialVersionUID = -506028161L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCalendarEvent calendarEvent = new QCalendarEvent("calendarEvent");

    public final BooleanPath allDay = createBoolean("allDay");

    public final QCalendar calendar;

    public final DateTimePath<java.time.LocalDateTime> deleteAt = createDateTime("deleteAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public QCalendarEvent(String variable) {
        this(CalendarEvent.class, forVariable(variable), INITS);
    }

    public QCalendarEvent(Path<? extends CalendarEvent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCalendarEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCalendarEvent(PathMetadata metadata, PathInits inits) {
        this(CalendarEvent.class, metadata, inits);
    }

    public QCalendarEvent(Class<? extends CalendarEvent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.calendar = inits.isInitialized("calendar") ? new QCalendar(forProperty("calendar"), inits.get("calendar")) : null;
    }

}

