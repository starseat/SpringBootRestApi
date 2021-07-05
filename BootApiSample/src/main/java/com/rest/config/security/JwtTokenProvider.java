package com.rest.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰을 생성 및 검증 모듈
 *  - JWT 토큰 생성 및 유효성 검증
 *  - JWT는 여러 암호화 알고리즘(SignatureAlgorithm.XXX) 와 비밀키(secretKey)를 가지고 토큰 생성
 *  - claim 정보에는 토큰에 부가적으로 실어 보낼 정보 세팅. 회원을 구분하는 값을 세팅하였다가 토큰이 들어오면 해당 값으로 회원 구분하여 리소스 제공
 *  - JWT토큰에 expire 시간 세팅. 토큰 발급 후 일정 시간 이후 토큰 만료
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 60 * 24; // 24시간만 토큰 유효

    private final UserDetailsService userDetailsService;
    
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 토큰 생성
     * @param userPk
     * @param roles
     * @return
     */
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)  // 데이터
                .setIssuedAt(now)  // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * JWT 토큰에서 회원 구별 정보 추출
     * @param token
     * @return
     */
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * JWT 토큰으로 인증 정보 조회
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Http Request의 Header에서 token 파싱: "X-AUTH-TOKEN: jwt토큰"
     *  - 제한된 리소스 요청시 http header에 토큰을 세팅하여 호출하면 유효성 검증하요 사용자 인증 가능 
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * JWT 코튼의 유효성 + 만료일자 확인
     * @param jwtToken
     * @return
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
