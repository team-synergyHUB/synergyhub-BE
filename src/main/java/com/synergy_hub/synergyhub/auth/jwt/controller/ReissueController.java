package com.synergy_hub.synergyhub.auth.jwt.controller;

import com.synergy_hub.synergyhub.auth.jwt.service.ReissueService;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<Void>> reissue(HttpServletRequest request,
        HttpServletResponse response) {

        return reissueService.reissue(request, response);
    }
}
