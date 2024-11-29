package com.synergy_hub.synergyhub.token.jwt;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/members/login") || path.equals("/") || path.equals("/members/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 클라이언트 요청 헤더에서 Authorization 찾기
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("token not exist");
            filterChain.doFilter(request, response);  //다음 필터 호출

            return;
        }

        //토큰 값 추출(Bearer 제거)
        String token = authorization.split(" ")[1];

        try {
            jwtTokenProvider.isExpired(token);
        } catch (ExpiredJwtException e) {

            log.info("JWT expired");

            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 payload에서 유저 정보 추출
            String username = jwtTokenProvider.getUsername(token);
            String role = jwtTokenProvider.getRole(token);
            MemberRole memberRole = MemberRole.fromString(role);

            // 세션 정보 설정
            Member member = Member.createSessionMember(username, null, memberRole);


            MemberDetails memberDetails = new MemberDetails(member);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                memberDetails, "", memberDetails.getAuthorities());

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            filterChain.doFilter(request, response);
        }


    }

}
