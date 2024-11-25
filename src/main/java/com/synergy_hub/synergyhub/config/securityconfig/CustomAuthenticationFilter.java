package com.synergy_hub.synergyhub.config.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();


    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/members/login"));  //url로 오면 필터 동작
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!isPost(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        loginRequestDto dto = objectMapper.readValue(request.getReader(), loginRequestDto.class);

        // ID, PASSWORD 가 있는지 확인
        if(!StringUtils.hasLength(dto.getUsername())
            || !StringUtils.hasLength(dto.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        // 처음에는 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
            dto.getUsername(),
            dto.getPassword()
        );

        // AuthenticationManager 에게 인증 처리
        Authentication authenticate = getAuthenticationManager().authenticate(token);

        return authenticate;
    }


    private boolean isPost(HttpServletRequest request) {

        if("POST".equals(request.getMethod())) {
            return true;
        }

        return false;
    }

    @Data
    public static class loginRequestDto {
        private String username;
        private String password;
    }
}
