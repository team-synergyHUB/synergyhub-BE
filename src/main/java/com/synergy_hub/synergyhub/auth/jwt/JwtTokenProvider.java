package com.synergy_hub.synergyhub.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret-key}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //토큰에서 유저정보(email 추출)
    public String getUsername(String token) {
        return getPayLoad(token, "username");
    }

    public String getRole(String token) {
        return getPayLoad(token, "role");
    }

    public String getLoginType(String token) {
        return getPayLoad(token, "loginType");
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

    private String getPayLoad(String token, String key) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(key, String.class);
    }

    //토큰 생성
    public String createJwtToken(String username, String role, Long expiredMs, String loginType) {

        return Jwts.builder()
            .claim("username", username)
            .claim("role", role)
            .claim("loginType", loginType)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

}
