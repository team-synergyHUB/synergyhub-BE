package com.synergy_hub.synergyhub.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy_hub.synergyhub.auth.jwt.service.RefreshService;
import com.synergy_hub.synergyhub.auth.oauth.service.CookieService;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.global.exception.ErrorResponseEntity;
import com.synergy_hub.synergyhub.member.dto.MemberLoginRequest;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshService;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    public LoginFilter(AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider, RefreshService refreshService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshService = refreshService;
        setFilterProcessesUrl("/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            MemberLoginRequest loginRequest = new ObjectMapper().readValue(
                request.getInputStream(), MemberLoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // Authentication 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

            // AuthenticationManager로 인증 처리
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new InvalidCookieException("Invalid Pass");
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authentication)
        throws IOException, ServletException {
        log.info("login Successfully");

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        String username = memberDetails.getUsername();//이메일 추출
        String nickname = memberDetails.getNickname();
        String profileImageUrl = memberDetails.getProfileImageUrl();
        Long userId = memberDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtTokenProvider.createJwtToken(
            "access", username, userId, role, 1200000L, "common"); //10분

        String refresh = jwtTokenProvider.createJwtToken(   //24시간
            "refresh", username, userId, role, 86400000L, "common");

        Date date = new Date(System.currentTimeMillis() + 86400000L);
        refreshService.saveRefresh(username, refresh, date.toString());

        //응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(CookieService.createCookie("refresh", refresh, 24*60*60));
        response.setStatus(HttpStatus.OK.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //회원 정보 json으로 응답
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("username", username);
        responseData.put("nickname", nickname);
        responseData.put("profileImageUrl", profileImageUrl);

        new ObjectMapper().writeValue(response.getWriter(), responseData);

    }


    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException {
        log.info("login Failed");

        ErrorCode errorCode = ErrorCode.INVALID_PASSWORD;
        ErrorResponseEntity errorResponse = ErrorResponseEntity.createErrorResponse(errorCode);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));


    }
}
