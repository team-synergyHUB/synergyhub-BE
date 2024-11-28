package com.synergy_hub.synergyhub.token.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synergy_hub.synergyhub.global.exception.ErrorCode;
import com.synergy_hub.synergyhub.global.exception.ErrorResponseEntity;
import com.synergy_hub.synergyhub.global.exception.ErrorResponseEntity.ErrorResponseEntityBuilder;
import com.synergy_hub.synergyhub.member.dto.MemberLoginRequest;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    public LoginFilter(AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String jwtToken = jwtTokenProvider.createJwtToken(
            username, role, 60 * 60 * 1000L);

        //응답 헤더에 추가
        response.addHeader("Authorization", "Bearer " + jwtToken);
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
