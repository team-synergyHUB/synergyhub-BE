package com.synergy_hub.synergyhub.auth.jwt;

import com.synergy_hub.synergyhub.auth.jwt.service.RefreshService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = Arrays.stream(request.getCookies())
            .filter(c -> c.getName().equals("refresh"))
            .findFirst()
            .get()
            .getValue();


        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtTokenProvider.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtTokenProvider.getCategory(refresh);
        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isExist = refreshService.isExistRefresh(refresh);
        if (!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //logout
        refreshService.deleteRefresh(refresh);
        log.info("logout successfully");

        // cookie reset
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }


}
