package com.synergy_hub.synergyhub.calendar.controller;

import com.synergy_hub.synergyhub.calendar.dto.CalendarEventRequestDTO;
import com.synergy_hub.synergyhub.calendar.dto.CalendarEventResponseDto;
import com.synergy_hub.synergyhub.calendar.service.CalendarService;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.member.controller.MemberController;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final MemberRepository memberRepository;

    // 일정 생성
    @PostMapping("/{calendarId}/events")
    public ResponseEntity<CalendarEventResponseDto> createEvent(
        @PathVariable Long calendarId,
        @RequestBody CalendarEventRequestDTO requestDTO) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 사용자 ID 가져오기
        Member currentMember = memberRepository.findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        CalendarEventResponseDto createdEvent = calendarService.createCalendarEvent(
            calendarId, requestDTO, currentMember.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    // 팀 일정 조회
    @GetMapping("/team/{teamId}/events")
    public ResponseEntity<List<CalendarEventResponseDto>> getTeamEvents(
        @PathVariable Long teamId) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 사용자 ID 가져오기
        Member currentMember = memberRepository.findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<CalendarEventResponseDto> teamEvents = calendarService.getTeamEvents(teamId, currentMember.getId());
        return ResponseEntity.ok(teamEvents);
    }

        // 개인 일정 조회
        @GetMapping("/my-events")
        public ResponseEntity<List<CalendarEventResponseDto>> getUserEvents() {
            Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 사용자 ID 가져오기
            List<CalendarEventResponseDto> userEvents = calendarService.getUserEvents(currentMemberId);
            return ResponseEntity.ok(userEvents);
        }


    // 일정 수정
    @PutMapping("/events/{calendarEventId}")
    public ResponseEntity<CalendarEventResponseDto> updateEvent(
        @PathVariable Long calendarEventId,
        @RequestBody CalendarEventRequestDTO requestDTO) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 사용자 ID 가져오기
        Member currentMember = memberRepository.findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        CalendarEventResponseDto updatedEvent = calendarService.updateEvent(calendarEventId, requestDTO, currentMember.getId());
        return ResponseEntity.ok(updatedEvent);
    }

    // 일정 삭제
    @DeleteMapping("/events/{calendarEventId}")
    public ResponseEntity<Void> deleteEvent(
        @PathVariable Long calendarEventId) {

        Long currentMemberId = MemberController.getAuthenticationMemberId(); // 인증된 사용자 ID 가져오기
        Member currentMember = memberRepository.findById(currentMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        calendarService.deleteEvent(calendarEventId, currentMember.getId());
        return ResponseEntity.noContent().build();
    }
}
