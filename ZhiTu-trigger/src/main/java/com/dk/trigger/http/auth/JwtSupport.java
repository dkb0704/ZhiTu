package com.dk.trigger.http.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 签发与解析，登录/注册成功后生成 token。
 */
@Component
public class JwtSupport {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-seconds}")
    private long expireSeconds;

    /**
     * 生成 JWT，subject 为 userId，claim "nickname" 可选。
     */
    public String createToken(Long userId, String nickname) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireSeconds * 1000);
        io.jsonwebtoken.JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry);
        if (nickname != null && !nickname.isEmpty()) {
            builder.claim("nickname", nickname);
        }
        return builder.signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 解析 JWT 获取 subject（userId）。
     */
    public Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        String sub = claims.getSubject();
        return sub == null ? null : Long.parseLong(sub);
    }
}
