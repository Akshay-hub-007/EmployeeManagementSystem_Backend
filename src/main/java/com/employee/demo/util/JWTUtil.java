package com.employee.demo.util;

import com.employee.demo.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {
    public  final String SECRET="my-super-secretkey-asjjnwjecnijwnejicnicwnicnweceyee-123@@@";
    private  final SecretKey key= Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME=1000*60*60*24;
    public String generateToken(String email,String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractToken(String token)
    {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {

        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }


    public boolean validateToken(String email, Users users, String token) {

        return email.equals(users.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return  extractClaims(token).getExpiration().before(new Date());
    }


//    public void extractToken(String token) {
//            retu
//    }
}
