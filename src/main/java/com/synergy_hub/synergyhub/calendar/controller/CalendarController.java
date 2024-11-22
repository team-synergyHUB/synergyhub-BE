package com.synergy_hub.synergyhub.calendar.controller;

import com.synergy_hub.synergyhub.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private CalendarService calendarService;
//
//    //일정생성
//    @PostMapping("")
//
//    //일정조회 ( 팀 캘린더 )
//    @GetMapping("")
//
//    //일정 조회 ( 개인 캘린더)
//    @GetMapping("")
//
//    //일정 수정
//    @PutMapping("")
//
//    //일정 삭제
//    @DeleteMapping("")



}
