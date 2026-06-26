package cn.sxu.enterprise.common.security.jwt;

import cn.sxu.enterprise.common.security.model.LoginUser;
import cn.sxu.enterprise.module.system.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(SysUser user) {
        Instant now = Instant.now();
        Instant expireAt = now.plus(Duration.ofMinutes(jwtProperties.getExpireMinutes()));

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("nickname", user.getNickname())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public LoginUser parseLoginUser(String token) {
        Claims claims = parseClaims(token);

        Long userId = Long.valueOf(String.valueOf(claims.get("userId")));
        String username = claims.getSubject();
        String nickname = String.valueOf(claims.get("nickname"));

        return new LoginUser(userId, username, nickname);
    }

    public Long getExpireSeconds() {
        return jwtProperties.getExpireMinutes() * 60;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}