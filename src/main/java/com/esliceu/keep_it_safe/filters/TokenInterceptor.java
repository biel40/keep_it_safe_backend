package com.esliceu.keep_it_safe.filters;


import com.esliceu.keep_it_safe.managers.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TokenInterceptor extends HandlerInterceptorAdapter {

    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String BEARER_PREFIX = "Bearer";

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        System.out.println("HOLA, SOY EL INTERCEPTOR DE BACKEND");

        try {

            // TODO: Null pointer exception

            String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);
            System.out.println(authenticationHeader);

            if (!authenticationHeader.startsWith(BEARER_PREFIX)) {
                // Si no tiene el prefijo Bearer, Unauthorized.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("UNAUTHORIZED REQUEST");
                return false;
            } else {

                // TODO: Verificarlo
                String token = request.getHeader("Authorization");
                System.out.println(token);


                return true;
            }

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        System.out.print("POST HANDLE INTERCEPTOR METHOD");

    }



}
