package com.synergy_hub.synergyhub.auth.jwt;

import com.synergy_hub.synergyhub.member.entity.Member;
import com.synergy_hub.synergyhub.member.entity.MemberDetails;
import com.synergy_hub.synergyhub.member.service.MemberService;
import com.synergy_hub.synergyhub.member.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret-key}") String secret,
        UserDetailsServiceImpl userDetailsService) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
        this.userDetailsService = userDetailsService;
    }

    //토큰에서 유저정보(email 추출)
    public String getUsername(String token) {
        return getPayLoad(token, "username");
    }

    public Long getUserId(String token) {
        return getPayLoadAsLong(token, "userId");
    }

    public String getRole(String token) {
        return getPayLoad(token, "role");
    }

    public String getLoginType(String token) {
        return getPayLoad(token, "loginType");
    }

    public String getCategory(String token) {
        return getPayLoad(token, "category");
    }

    //토큰 소멸 확인
    public Boolean isExpired(String token) {

        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        System.out.println("claims.getExpiration() = " + claims.getExpiration());

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration().before(new Date());

    }


    private <T> T getPayLoad(String token, String key, Class<T> type) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(key, type);
    }

    private String getPayLoad(String token, String key) {
        return getPayLoad(token, key, String.class);
    }

    private Long getPayLoadAsLong(String token, String key) {
        return getPayLoad(token, key, Long.class);
    }


    //토큰 생성
    public String createJwtToken(
        String category, String username, Long userId, String role, Long expiredMs, String loginType) {

        return Jwts.builder()
            .claim("category", category)
            .claim("username", username)
            .claim("userId", userId)
            .claim("role", role)
            .claim("loginType", loginType)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

    public Authentication createAuthentication(String accessToken) {

        Long userId = getPayLoadAsLong(accessToken, "userId");
        String username = getPayLoad(accessToken, "username");
        String role = getPayLoad(accessToken, "role");

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
