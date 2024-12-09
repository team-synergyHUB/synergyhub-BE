package com.synergy_hub.synergyhub.calendar.controller;

import com.synergy_hub.synergyhub.calendar.dto.CalendarEventRequestDTO;
import com.synergy_hub.synergyhub.calendar.dto.CalendarEventResponseDto;
import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import com.synergy_hub.synergyhub.calendar.service.CalendarService;
import com.synergy_hub.synergyhub.global.CommonApiDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
@Tag(name = "Calendar", description = "캘린더 관련 API")
public class CalendarController {

    private final CalendarService calendarService;

    //일정생성
    @CommonApiDocs(summary = "일정 생성", description = "특정 팀이나 개인 일정에 이벤트를 생성합니다.")
    @PostMapping("/{calendarId}/events")
    public ResponseEntity<CalendarEventResponseDto> createEvent(@PathVariable Long calendarId,
        @RequestParam Long memberId,
        @RequestBody CalendarEventRequestDTO requestDTO){

        CalendarEventResponseDto createdEvent = calendarService.createCalendarEvent(
            calendarId, requestDTO, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    //일정조회 ( 팀 캘린더 )
    @CommonApiDocs(summary = "팀 캘린더 일정 조회", description = "특정 팀의 일정을 조회합니다.")
    @GetMapping("/team/{teamId}/events")
    public ResponseEntity<List<CalendarEventResponseDto>> getTeamEvents(@PathVariable Long teamId,
        @RequestParam Long memberId) {
        List<CalendarEventResponseDto> teamEvents = calendarService.getTeamEvents(
            teamId, memberId);

        return ResponseEntity.ok(teamEvents);
    }

    //일정 조회 ( 개인 캘린더)
    @CommonApiDocs(summary = "개인 캘린더 일정 조회", description = "특정 회원의 개인 일정을 조회합니다.")
    @GetMapping("/user/{memberId}/events")
    public ResponseEntity<List<CalendarEventResponseDto>> getUserEvents(@PathVariable Long memberId){
        List<CalendarEventResponseDto> userEvents = calendarService.getUserEvents(memberId);
        return ResponseEntity.ok(userEvents);
    }

    //일정 수정
    @CommonApiDocs(summary = "일정 수정", description = "기존 일정의 정보를 수정합니다.")
    @PutMapping("/events/{calendarEventId}")
    public ResponseEntity<CalendarEventResponseDto> updateEvent(
        @PathVariable Long calendarEventId,
        @RequestParam Long memberId,
        @RequestBody CalendarEventRequestDTO requestDTO){

        CalendarEventResponseDto updateEvent = calendarService.updateEvent(
            calendarEventId, requestDTO, memberId);
        return ResponseEntity.ok(updateEvent);
    }

    //일정 삭제
    @CommonApiDocs(summary = "일정 삭제", description = "기존 일정을 삭제합니다.")
    @DeleteMapping("/events/{calendarEventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long calendarEventId) {
        calendarService.deleteEvent(calendarEventId);
        return ResponseEntity.noContent().build();
    }

}
