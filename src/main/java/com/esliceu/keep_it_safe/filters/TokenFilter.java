package com.esliceu.keep_it_safe.filters;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TokenFilter extends OncePerRequestFilter {

    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {

        System.out.println(SECRET_KEY);

        try {
            if (!existsJWTToken(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                Claims claims = validateToken(request);
                request.setAttribute("claims", claims);
                // Añadir media hora más al Token (refreshToken?)
            }

            chain.doFilter(request, response);


        } catch (JwtException | ServletException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
    }

    private Claims validateToken(HttpServletRequest request) {

        String jwtToken = request.getHeader(HEADER_AUTHORIZATION).replace(BEARER_PREFIX, "");

        return Jwts.parser()
            .setSigningKey(SECRET_KEY.getBytes() )
            .parseClaimsJws(jwtToken)
            .getBody();
    }

    private boolean existsJWTToken(HttpServletRequest request) {

        String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authenticationHeader == null || !authenticationHeader.startsWith(BEARER_PREFIX))
            return false;

        return true;

    }


}
