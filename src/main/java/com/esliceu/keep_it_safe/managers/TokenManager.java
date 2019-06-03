package com.esliceu.keep_it_safe.managers;

import com.esliceu.keep_it_safe.entities.User;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String getJWTToken(User user) {

        System.out.println(user.toString());
        System.out.println(user.getImageUrl());

        String token = Jwts
                .builder()
                .setSubject(user.toString())
                .claim("role", user.getRol_user())
                .claim("name", user.getName())
                .claim("surnames", user.getSurnames())
                .claim("imageUrl", user.getImageUrl())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;

    }

    public String validateToken(String token) {
        try {

            Gson gson = new Gson();

            //Lo transformamos a Json
            return gson.toJson(Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody());

        } catch (io.jsonwebtoken.JwtException e) {
            System.out.println(e);
            return null;
        }
    }
}
