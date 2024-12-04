package com.synergy_hub.synergyhub.auth.oauth.controller;

import com.synergy_hub.synergyhub.auth.oauth.service.Oauth2JwtHeaderService;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 최초 로그인시 서버가 쿠키로 보낸 token을 클라이언트가 다시 서버에 보냄 서버는 다시 JWT token을 헤더에 담아서 클라이언트로 응답 이후 header 통해 JWT
 * token 으로 통신 가능
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {

    private final Oauth2JwtHeaderService oauth2JwtHeaderService;

    @PostMapping("/oauth2/jwt-header")
    public ResponseEntity<ApiResponse<Void>> oauth2jwtHeader(
        HttpServletRequest request, HttpServletResponse response) {

        log.info("====jwtHeaderService 호출====");
        return oauth2JwtHeaderService.handleJwtHeader(request, response);
    }
}
