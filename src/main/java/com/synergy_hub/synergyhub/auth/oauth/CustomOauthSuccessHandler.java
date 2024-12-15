package com.synergy_hub.synergyhub.auth.oauth;

import com.synergy_hub.synergyhub.auth.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.auth.jwt.service.RefreshService;
import com.synergy_hub.synergyhub.auth.oauth.dto.CustomOauth2User;
import com.synergy_hub.synergyhub.auth.oauth.service.CookieService;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

//        CustomOauth2User customUserDetails = (CustomOauth2User) authentication.getPrincipal();
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();


        String email = memberDetails.getUsername();  //이메일을 username으로 통일
        Long userId = memberDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtTokenProvider.createJwtToken(
            "access", email, userId, role, 60 * 20 * 1000L, "social");

        String refresh = jwtTokenProvider.createJwtToken(
            "refresh", email, userId, role, 86400000L, "social");

        Date date = new Date(System.currentTimeMillis() + 86400000L);
        refreshService.saveRefresh(email, refresh, date.toString());

        //쿠키에 JWT 담아서 응답
        response.addCookie(CookieService.createCookie(
            "Authorization", access, 24*60*60));  //access token

        response.addCookie(CookieService.createCookie(
            "refresh", refresh, 24*60*60));  //Refresh token

        response.sendRedirect("http://localhost:3000/oauth2-jwt-header");
    }
}
