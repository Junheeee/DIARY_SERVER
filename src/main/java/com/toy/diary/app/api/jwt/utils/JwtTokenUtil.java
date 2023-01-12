package com.toy.diary.app.api.jwt.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

//@Component
@Log4j2
public class JwtTokenUtil implements Serializable {

//    private static final long serialVersionUID = -2550185165626007488L;
//
//    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5시간
//    //    public static final long JWT_TOKEN_VALIDITY = 10; // 10초
//    public static final long JWT_REFRESH_TOKEN_VALIDTY = 21* 24 * 60 * 60; //21일
//
//    @Value("${spring.jwt.secret}")
//    private String secret;
//    @Value("${spring.jwt.prefix}")
//    private String JWT_PREFIX;
//
//    //retrieve username from jwt token
//    // jwt token으로부터 username을 획득한다.
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date from jwt token
//    // jwt token으로부터 만료일자를 알려준다.
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//    //for retrieveing any information from token we will need the secret key
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    //check if the token has expired
//    // 토큰이 만료되었는지 확인한다.
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
////        log.debug(">>>" + expiration);
//        return expiration.before(new Date());
//    }
//
//    //generate token for user
//    // 유저를 위한 토큰을 발급해준다.
//    public String generateToken(JwtUserModel userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", userDetails.getUserId());
//        claims.put("cstmrSno", userDetails.getCstmrSno());
//        claims.put("auth", userDetails.getAuth());
//        claims.put("group", userDetails.getGroup());
//        return doGenerateToken(claims, userDetails.getUsername());
//    }
//
//
//    public String generateRefreshToken(JwtUserModel userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", userDetails.getUserId());
//        claims.put("cstmrSno", userDetails.getCstmrSno());
//        return doGenerateRefreshToken(claims, userDetails.getUsername());
//    }
//
//
//    //while creating the token -
//    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    //2. Sign the JWT using the HS512 algorithm and secret key.
//    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    //   compaction of the JWT to a URL-safe string
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }
//
//    private String doGenerateRefreshToken(Map<String , Object> claims , String subject) {
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDTY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }
//
//    //validate token
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    public String getUserIdByToken() {
//        HttpServletRequest rq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = rq.getHeader("Authorization");
//
//        if(token == null || "".equals(token)) return null;
//
//        token = token.substring(JWT_PREFIX.length()).trim();
//        String userId = getUsernameFromToken(token);
//
//        return userId;
//    }
//    public Integer getCstmrSnoByToken() {
//        HttpServletRequest rq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = rq.getHeader("Authorization");
//
//        if(token == null || "".equals(token)) return null;
//
//        token = token.substring(JWT_PREFIX.length()).trim();
//        Claims payload = getAllClaimsFromToken(token);
//        Integer cstmrSno = payload.get("cstmrSno",Integer.class);
//
//        return cstmrSno;
//    }
}
