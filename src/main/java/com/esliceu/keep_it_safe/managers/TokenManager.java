package com.esliceu.keep_it_safe.managers;

import com.esliceu.keep_it_safe.entities.User;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TokenManager {
    public static List<String> blackListToken = new ArrayList<>();
    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String getJWTToken(User user) {

        String token = Jwts
                .builder()
                .claim("rol", user.getRol_user())
                .claim("name", user.getName())
                .claim("surnames", user.getSurnames())
                .claim("imageUrl", user.getImageUrl())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;

    }

    public String[] validateToken(String token) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            Date date = new Date(System.currentTimeMillis() + 3600000);
            claims.setExpiration(date);

            String userInfoJson = new Gson().toJson(claims);

            return new String[]{userInfoJson, this.refreshToken(claims)};

        } catch (io.jsonwebtoken.JwtException e) {
            System.out.println(e);
            return null;
        }
    }

    private String refreshToken(Claims claims) {
        String token = Jwts
                .builder()
                .claim("rol", claims.get("rol"))
                .claim("name", claims.get("name"))
                .claim("surnames", claims.get("surnames"))
                .claim("imageUrl", claims.get("imageUrl"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;
    }
}
