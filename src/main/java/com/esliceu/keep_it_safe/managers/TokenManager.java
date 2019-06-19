package com.esliceu.keep_it_safe.managers;

import com.esliceu.keep_it_safe.Constants;
import com.esliceu.keep_it_safe.entities.User;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@EnableScheduling
@Component
public class TokenManager {

    public static List<String> blackListToken = new ArrayList<>();

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String getJWTToken(User user) {

        String token = Jwts
                .builder()
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .claim("rol_user", user.getRol_user())
                .claim("name", user.getName())
                .claim("surnames", user.getSurnames())
                .claim("imageUrl", user.getImageUrl())
                .claim("userLoginSocial",user.isUserLoginSocial())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;

    }

    public String[] validateToken(String token) {
        try {

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            if(!this.isExpiredToken(claims)){

                Date date = new Date(System.currentTimeMillis() + Constants.EXPIRATION_TOKEN);
                claims.setExpiration(date);

                String userInfoJson = new Gson().toJson(claims);

                return new String[]{userInfoJson, this.refreshToken(claims)};

            } else  {
                return null;
            }

        } catch (io.jsonwebtoken.JwtException e) {
            return null;
        }
    }

    private boolean validateTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            return this.isExpiredToken(claims);

        } catch (io.jsonwebtoken.JwtException e) {
            System.out.println(e);
            return false;
        }
    }

    private boolean isExpiredToken(Claims claims){
        Date expiration_token = claims.getExpiration();
        Date now = new Date(System.currentTimeMillis());
        return !now.before(expiration_token);
    }

    private String refreshToken(Claims claims) {
        String token = Jwts
                .builder()
                .claim("userId", claims.get("userId"))
                .claim("email", claims.get("email"))
                .claim("rol_user", claims.get("rol_user"))
                .claim("name", claims.get("name"))
                .claim("surnames", claims.get("surnames"))
                .claim("imageUrl", claims.get("imageUrl"))
                .claim("userLoginSocial",claims.get("userLoginSocial"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TOKEN))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return token;
    }

    @Scheduled(fixedRate = Constants.EXPIRATION_TOKEN)
    private void removeOldTokens() {
        List<String> tokensToRemove = new ArrayList<>();
        for(String token : TokenManager.blackListToken) {
            boolean isValid = this.validateTokenExpired(token);
            if (!isValid) {
                tokensToRemove.add(token);
            }
        }
        TokenManager.blackListToken.removeAll(tokensToRemove);
    }
}
