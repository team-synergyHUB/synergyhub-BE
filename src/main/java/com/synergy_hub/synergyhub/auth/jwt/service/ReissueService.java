package com.synergy_hub.synergyhub.auth.jwt.service;

import com.synergy_hub.synergyhub.auth.exception.CookieNotFoundException;
import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.auth.oauth.service.CookieService;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.global.response.ApiResponse;
import com.synergy_hub.synergyhub.global.response.ApiResponseBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Access token 이 만료되어 클라이언트로부터 Refresh token 이 오면
 * refresh token 을 검증하고 Access token 새로 발급
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshService;

    @Transactional
    public ResponseEntity<ApiResponse<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CookieNotFoundException(ErrorCode.COOKIE_NOT_FOUND);
        }

        String refresh = Arrays.stream(cookies)
                .filter((cookie) -> cookie.getName().equals("refresh"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new CookieNotFoundException(ErrorCode.COOKIE_NOT_FOUND));

        log.info("refresh 토큰 : {}", refresh);

        // 쿠키에 refresh 토큰 x
        if (refresh == null) {
            return ApiResponseBuilder.fail("refresh token not exist", HttpStatus.BAD_REQUEST);
        }

        // 만료된 토큰은 payload 읽을 수 없음 -> ExpiredJwtException 발생
        try {
            jwtTokenProvider.isExpired(refresh);
        } catch(ExpiredJwtException e){
            return ApiResponseBuilder.fail("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰이 아님
        String category = jwtTokenProvider.getCategory(refresh);
        if(!category.equals("refresh")) {
            return ApiResponseBuilder.fail("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtTokenProvider.getUsername(refresh);
        String role = jwtTokenProvider.getRole(refresh);
        Long userId = jwtTokenProvider.getUserId(refresh);
        String loginType = jwtTokenProvider.getLoginType(refresh);

        // refresh 토큰 조회
        boolean isExist = refreshService.isExistRefresh(refresh);

        // DB 확인 (혹은 블랙리스트 처리된 리프레시 토큰)
        if(!isExist) {
            return ApiResponseBuilder.fail("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // new tokens
        String newAccess = jwtTokenProvider.createJwtToken(
                "access", username, userId,  role, 600000L, loginType);

        String newRefresh = jwtTokenProvider.createJwtToken(
                "refresh", username, userId,  role, 86400000L, loginType);

        // 기존 refresh DB 삭제, 새로운 refresh 저장
        Date date = new Date(System.currentTimeMillis() + 86400000L);
        refreshService.deleteRefresh(refresh);
        refreshService.saveRefresh(username, newRefresh, date.toString());

        response.addHeader("Authorization", "Bearer " + newAccess);
        CookieService.addCookieWithSameSite(response, "refresh", newRefresh, 24 * 60 * 60);
//        response.addCookie(CookieService.createCookie("refresh", newRefresh, 24*60*60));

        return ApiResponseBuilder.success("create Access token successfully", null,
                HttpStatus.OK);
    }

}


