package com.synergy_hub.synergyhub.auth.jwt;

import com.sun.net.httpserver.HttpsParameters;
import com.synergy_hub.synergyhub.auth.oauth.dto.CustomOauth2User;
import com.synergy_hub.synergyhub.auth.oauth.dto.UserDto;
import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.entity.MemberRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        return path.equals("/members/login") || path.equals("/") || path.equals("/members/signup")
            || path.equals("/ws");
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

            log.info("Access token expired");

            PrintWriter writer = response.getWriter();
            writer.print("Access token expired");

            //access 토큰 만료시 401 에러
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {

            //토큰이 Access 토큰인지 검증
            String category = jwtTokenProvider.getCategory(token);

            if (!category.equals("access")) {
                PrintWriter writer = response.getWriter();
                writer.print("Invalid access token");

                //access 토큰 만료시 401 에러 - 클라이언트 처리
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //payload에서 정보 추출
            String username = jwtTokenProvider.getUsername(token);
            Long userId = jwtTokenProvider.getUserId(token);
            String role = jwtTokenProvider.getRole(token);
            MemberRole memberRole = MemberRole.fromString(role);
            String loginType = jwtTokenProvider.getLoginType(token);

            Member member = Member.createSessionMember(userId, username, null, memberRole);
            MemberDetails memberDetails = new MemberDetails(member);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    memberDetails, "", memberDetails.getAuthorities());

                //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

//
//            // 세션 정보 설정 - JWT 일반 로그인
//            if("common".equals(loginType)) {
//                Member member = Member.createSessionMember(userId, username, null, memberRole);
//                MemberDetails memberDetails = new MemberDetails(member);
//                Authentication authToken = new UsernamePasswordAuthenticationToken(
//                    memberDetails, "", memberDetails.getAuthorities());
//
//                //세션에 사용자 등록
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                filterChain.doFilter(request, response);
//            }
//            else { //Oauth2 로그인 - loginType=social
//                UserDto userDto = new UserDto();
//                userDto.setEmail(username);
//                userDto.setUserId(userId);
//                userDto.setRole(memberRole);
//                CustomOauth2User customUserDetails = new CustomOauth2User(userDto);
//
//                Authentication authToken = new UsernamePasswordAuthenticationToken(
//                    customUserDetails, "", customUserDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                filterChain.doFilter(request, response);
//            }


        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            filterChain.doFilter(request, response);
        }


    }

}
