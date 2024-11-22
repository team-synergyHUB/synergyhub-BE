package com.synergy_hub.synergyhub.calendar.service;

import com.synergy_hub.synergyhub.calendar.dto.CalendarEventRequestDTO;
import com.synergy_hub.synergyhub.calendar.dto.CalendarEventResponseDto;
import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import com.synergy_hub.synergyhub.calendar.repository.CalendarEventRepository;
import com.synergy_hub.synergyhub.calendar.repository.CalendarRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;

    //일정 생성
    @Transactional
    public CalendarEventResponseDto createCalendarEvent(Long calendarId, CalendarEventRequestDTO requestDTO){
        //예외처리 예정
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow();

        CalendarEvent event = CalendarEvent.builder()
            .calendar(calendar)
            .title(requestDTO.getTitle())
            .startDate(requestDTO.getStartDate())
            .endDate(requestDTO.getEndDate())
            .allDay(requestDTO.isAllDay())
            .build();

        //캘린더에 일정 추가
        calendar.addEvent(event);

        // 새로 생성된 일정 저장
        calendarEventRepository.save(event);

        //응답 dto로 변환 (색상 후에 추가 예정)
        return convertToResponseDto(event);
    }

    // 일정 조회 ( 팀 캘린더 )
    @Transactional(readOnly = true)
    public List<CalendarEventResponseDto> getTeamEvents(Long teamId){
        return calendarEventRepository.findByTeamId(teamId).stream()
            // 색상 포함 예정
            .map(event-> convertToResponseDto(event))
            .collect(Collectors.toList());
    }

    // 일정조회 ( 개인 캘린더)
    @Transactional(readOnly = true)
    public List<CalendarEventResponseDto> getUserEvents(Long memberId) {
        return calendarEventRepository.findAllEventsForUser(memberId).stream()
            //색상 포함 예정
            .map(event -> convertToResponseDto(event))
            .collect(Collectors.toList());
    }

    // 일정 수정
    @Transactional
    public CalendarEventResponseDto updateEvent(Long calendarEventId, CalendarEventRequestDTO requestDTO){
        //예외처리 예정
        CalendarEvent event = calendarEventRepository.findById(calendarEventId)
            .orElseThrow();

        event.updateEventDetails(requestDTO.getTitle(), requestDTO.getStartDate(), requestDTO.getEndDate(), requestDTO.isAllDay());

        //색상 추가예정
        return convertToResponseDto(event);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long calendarEventId) {
        //예외처리 예정
        CalendarEvent event = calendarEventRepository.findById(calendarEventId)
            .orElseThrow();

        event.markAsDeleted();

        calendarEventRepository.save(event);
    }

    //응답 dto 변환 메서드(색상 추가 예정)
    private CalendarEventResponseDto convertToResponseDto(CalendarEvent event/*, String color*/){

        return CalendarEventResponseDto.builder()
            .id(event.getId())
            .title(event.getTitle())
            .startDate(event.getStartDate())
            .endDate(event.getEndDate())
            .allDay(event.isAllDay())
//            .color(color)
            .build();
    }







}
