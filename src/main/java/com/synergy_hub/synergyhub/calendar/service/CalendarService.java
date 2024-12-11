package com.synergy_hub.synergyhub.calendar.service;

import com.synergy_hub.synergyhub.calendar.dto.CalendarEventRequestDTO;
import com.synergy_hub.synergyhub.calendar.dto.CalendarEventResponseDto;
import com.synergy_hub.synergyhub.calendar.entity.Calendar;
import com.synergy_hub.synergyhub.calendar.entity.CalendarEvent;
import com.synergy_hub.synergyhub.calendar.repository.CalendarEventRepository;
import com.synergy_hub.synergyhub.calendar.repository.CalendarRepository;
import com.synergy_hub.synergyhub.global.exception.CustomException;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.team.service.MemberTeamService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final MemberTeamService memberTeamService;


    //일정 생성
    @Transactional
    public CalendarEventResponseDto createCalendarEvent(
        Long calendarId, CalendarEventRequestDTO requestDTO, Long memberId){
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_NOT_FOUND));

        memberTeamService.validateMemberOfTeam(memberId, calendar.getTeam().getId());

        String color = memberTeamService.getTeamColor(memberId, calendar.getTeam().getId());

        CalendarEvent event = CalendarEvent.builder()
            .calendar(calendar)
            .title(requestDTO.getTitle())
            .startDate(requestDTO.getStartDate())
            .endDate(requestDTO.getEndDate())
            .build();

        //캘린더에 일정 추가
        calendar.addEvent(event);

        // 새로 생성된 일정 저장
        calendarEventRepository.save(event);


        //응답 dto로 변환 (색상 후에 추가 예정)
        return convertToResponseDto(event,color);
    }

    // 일정 조회 ( 팀 캘린더 )
    public List<Map<String, Object>> getTeamEventsAsFullCalendarFormat(Long teamId, Long memberId) {
        // 팀원 검증
        memberTeamService.validateMemberOfTeam(memberId, teamId);

        // 팀의 이벤트를 가져오고 색상을 설정한 후, FullCalendar 형식으로 변환
        List<CalendarEvent> calendarEvents = calendarEventRepository.findEventByTeam(teamId);
        String color = memberTeamService.getTeamColor(memberId, teamId);

        // CalendarEvent -> CalendarEventResponseDto -> FullCalendar 형식으로 변환
        return calendarEvents.stream()
            .map(event -> convertToFullCalendarFormat(convertToResponseDto(event, color)))  // 두 단계 변환
            .collect(Collectors.toList());
    }


    // 일정조회 ( 개인 캘린더)
    public List<CalendarEventResponseDto> getUserEvents(Long memberId) {

        Map<Long, String> allColors = memberTeamService.getAllTeamColor(memberId);

        return calendarEventRepository.findAllEventsForUser(memberId).stream()
            .map(event -> {
                Long teamId = event.getCalendar().getTeam().getId();
                String color = allColors.getOrDefault(teamId, "#000000");
                return convertToResponseDto(event, color);
            })
            .collect(Collectors.toList());
    }

    // 일정 수정
    @Transactional
    public CalendarEventResponseDto updateEvent(
        Long calendarEventId, CalendarEventRequestDTO requestDTO,Long memberId){
        //예외처리 예정
        CalendarEvent event = calendarEventRepository.findById(calendarEventId)
            .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_EVENT_NOT_FOUND));

        if (event.isDelete()) {
            throw new CustomException(ErrorCode.CALENDAR_EVENT_NOT_FOUND);
        }
        memberTeamService.validateMemberOfTeam(memberId, event.getCalendar().getTeam().getId());

        event.updateEventDetails(requestDTO.getTitle(), requestDTO.getStartDate(),
            requestDTO.getEndDate());

        String color = memberTeamService.getTeamColor(memberId, event.getCalendar().getTeam().getId());

        //색상 추가예정
        return convertToResponseDto(event, color);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long calendarEventId, Long memberId) {
        CalendarEvent event = calendarEventRepository.findById(calendarEventId)
            .orElseThrow(() -> new CustomException(ErrorCode.CALENDAR_EVENT_NOT_FOUND));

        if (event.isDelete()) { throw new CustomException(ErrorCode.CALENDAR_EVENT_NOT_FOUND); }

        memberTeamService.validateMemberOfTeam(memberId, event.getCalendar().getTeam().getId());

        event.markAsDeleted();

        calendarEventRepository.save(event);
    }

    //응답 dto 변환 메서드(색상 추가 예정)
    private CalendarEventResponseDto convertToResponseDto(CalendarEvent event, String color){

        return CalendarEventResponseDto.builder()
            .id(event.getId())
            .title(event.getTitle())
            .startDate(event.getStartDate())
            .endDate(event.getEndDate())
            .color(color)
            .build();
    }

    public Map<String, Object> convertToFullCalendarFormat(CalendarEventResponseDto dto) {
        return Map.of(
            "id", dto.getId(),
            "title", dto.getTitle(),
            "start", dto.getStartDate().toString(),
            "end", dto.getEndDate() != null ? dto.getEndDate().toString() : null,
            "color", dto.getColor()
        );
    }


}
