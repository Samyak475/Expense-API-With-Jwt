package com.codeartist.expenseapi.services;

import com.codeartist.expenseapi.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Autowired
    UserService userService;
    private final String SECRET_KEY = "aa16aa7e49f526bb4c6a66e60d7a283fb2fc7cfa3adc3813664a4dec";
    SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    public String generateToken(String username){
        Map<String , Object> claims = new HashMap<>();
        return createToken( username ,claims);
    }
    private String createToken(String username,Map<String,Object>claims){
        return Jwts.builder()
                .setClaims(claims)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+60*60*24*7*1000L))
                .compact();
    }

    public Boolean isValidToken(String token, UserEntity user){
        return !isTokenExpired(token)&getSubject(token).equalsIgnoreCase(user.getUsername());
    }
    public  Boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public Claims  parseToken(String token){
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build().parseSignedClaims(token)
                .getBody();
    }
    public String getSubject(String token){
        return parseToken(token).getSubject();
    }
    public Date getExpiration(String token){
        return  parseToken(token).getExpiration();
    }


}
