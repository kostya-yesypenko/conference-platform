package org.chnu.confplatform.back.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

@Service
@Log
public class JwtService {

    @Value("${org.chnu.confplatform.back.jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${org.chnu.confplatform.back.jwt.jwtExpirationMinutes}")
    public int jwtExpirationMinutes;

    @Value("${org.chnu.confplatform.back.jwt.refreshExpirationMinutes}")
    public int refreshTokenExpirationMinutes;


    public String generateToken(String email) {
        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(jwtExpirationMinutes);
        Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
