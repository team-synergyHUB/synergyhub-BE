package com.synergy_hub.synergyhub.auth.oauth.service;

import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import com.synergy_hub.synergyhub.member.dto.MemberResponseDto;
import com.synergy_hub.synergyhub.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 쿠키는 만료시키고, token은 헤더에 전달
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2JwtHeaderService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public ResponseEntity<ApiResponse<Void>> handleJwtHeader(
        HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String jwtAccessToken = null;

        if (cookies == null) {
            return ApiResponseBuilder.fail("cookie not exist", HttpStatus.BAD_REQUEST);
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                jwtAccessToken = cookie.getValue();
            }
        }

        if (jwtAccessToken == null) {
            return ApiResponseBuilder.fail("cookie not exist", HttpStatus.BAD_REQUEST);
        }

        //jwt parsing
        String username = jwtTokenProvider.getUsername(jwtAccessToken);
        Long userId = jwtTokenProvider.getUserId(jwtAccessToken);

        response.addCookie(CookieService.createCookie("Authorization", null, 0));
        response.addHeader("Authorization", "Bearer " + jwtAccessToken);

        return ApiResponseBuilder.success(
            "JwtHeaderService Successfully", null, HttpStatus.OK);
    }

}
