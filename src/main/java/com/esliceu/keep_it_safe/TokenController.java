package com.esliceu.keep_it_safe;

import com.esliceu.keep_it_safe.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenController {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String getJWTToken(User user) {

        System.out.println(SECRET_KEY);
        System.out.println(user.toString());

        String token = Jwts
                .builder()
                .setSubject(user.toString())
                .claim("role", user.getRol_user())
                .claim("name", user.getName())
                .claim("surnames", user.getSurnames())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;

    }
}
