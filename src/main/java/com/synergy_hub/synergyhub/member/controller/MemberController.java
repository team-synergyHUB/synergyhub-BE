package com.synergy_hub.synergyhub.member.controller;

import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import com.synergy_hub.synergyhub.member.dto.MemberAddRequest;
import com.synergy_hub.synergyhub.member.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Object>>> signUp(@RequestBody MemberAddRequest request) {
        Long savedMemberId = memberService.save(request);

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", savedMemberId);

        return ApiResponseBuilder.success("Member created successfully", payLoad,
            HttpStatus.CREATED);

    }

//    @PostMapping("/signUp")
//    public ResponseEntity<Map<String, Object>> signUp(@RequestBody AddMemberRequest request) {
//        Long savedMemberId = memberService.save(request);
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("id", savedMemberId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "Member created successfully");
//        response.put("payload", payload);
//
//        // 201 CREATED 상태 코드로 응답 반환
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//    }
}
