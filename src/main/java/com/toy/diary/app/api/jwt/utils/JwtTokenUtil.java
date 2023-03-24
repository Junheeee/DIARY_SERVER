package com.toy.diary.app.api.jwt.utils;


import com.toy.diary.app.api.jwt.model.JwtUserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
//
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5시간
//    //    public static final long JWT_TOKEN_VALIDITY = 10; // 10초
    public static final long JWT_REFRESH_TOKEN_VALIDTY = 21* 24 * 60 * 60; //21일
//
    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.prefix}")
    private String JWT_PREFIX;

    //retrieve username from jwt token
    // jwt token으로부터 username을 획득한다.
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    // jwt token으로부터 만료일자를 알려준다.
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        log.info(token);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // 토큰이 만료되었는지 확인한다.
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

//     유저를 위한 토큰을 발급해준다.
    public String generateToken(JwtUserModel userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUserId());
        claims.put("cstmrSno", userDetails.getCstmrSno());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(JwtUserModel userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUserId());
        claims.put("cstmrSno", userDetails.getCstmrSno());
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String doGenerateRefreshToken(Map<String , Object> claims , String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDTY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUserIdByToken() {
        HttpServletRequest rq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = rq.getHeader("Authorization");
        log.info(token);

        if(token == null || "".equals(token)) return null;

        token = token.substring(JWT_PREFIX.length()).trim();
        String userId = getUsernameFromToken(token);

        return userId;
    }

    public Integer getCstmrSnoByToken() {
        HttpServletRequest rq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = rq.getHeader("Authorization");

        if(token == null || "".equals(token)) return null;

        token = token.substring(JWT_PREFIX.length()).trim();
        Claims payload = getAllClaimsFromToken(token);
        Integer cstmrSno = payload.get("cstmrSno",Integer.class);

        return cstmrSno;
    }
}
