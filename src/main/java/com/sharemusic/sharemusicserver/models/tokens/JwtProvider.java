package com.sharemusic.sharemusicserver.models.tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Component
@PropertySource("classpath:secure.properties")
public class JwtProvider {

    public static String jwtKey = "AUTHENTICATION_KEY";
    public static byte[] secret;
    public static int refreshTokenExpiration = 1000 * 60 * 60 * 24 * 15;
    public static int accessTokenExpiration = 1000 * 60 * 60;




    JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    };


    private Key key;


    public Jwt createJwt(Map<String, Object> claims) {
        String assessToken = createToken(claims, getExpirationAssessToken());
        String refreshToken = createToken(claims, getExpirationRefreshToken());

        return Jwt.builder()
                .assessToken(assessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAccessTokenExpiration (Claims claims) {

        System.out.println("isAccessTokenExpiration 실행");

        Date tokenExpiration =  claims.getExpiration();

        System.out.println("토큰 만료 시간 : " + tokenExpiration);
        System.out.println("현재 시간 : " + new Date());
        System.out.println("만료 됬나? " + tokenExpiration.before(new Date()));

        return tokenExpiration.before(new Date());
    }




    private String createToken(Map<String, Object> claims, Date expireDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    private Date getExpirationAssessToken() {
        long expireTimeMils = JwtProvider.accessTokenExpiration;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }

    private Date getExpirationRefreshToken() {
        long expireTimeMils = JwtProvider.refreshTokenExpiration;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }



}
