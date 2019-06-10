package com.esliceu.keep_it_safe.filters;


import com.esliceu.keep_it_safe.managers.TokenManager;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private final String HEADER_AUTHORIZATION = "Authorization";
    private final String IS_THE_REQUES = "IsTheRequest";
    private final String BEARER_PREFIX = "Bearer";

    @Autowired
    private TokenManager tokenManager;

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        System.out.println("HOLA, SOY EL INTERCEPTOR DE BACKEND");

        try {
            String isTheRequest = request.getHeader(IS_THE_REQUES);
            System.out.println(isTheRequest);

            if (isTheRequest != null && isTheRequest.equals("true")) {
                String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);
                if (authenticationHeader.startsWith(BEARER_PREFIX)) {
                    List<String> blackList = TokenManager.blackListToken;

                    String token = authenticationHeader.split(" ")[1];

                    System.out.println(token);

                    if (blackList.contains(token)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {

                        String[] tokenVerified = tokenManager.validateToken(token + "wbfowef");
                        if (tokenVerified != null) {
                            response.setHeader("user", tokenVerified[0]);
                            response.setHeader("token", tokenVerified[1]);
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    System.out.println("UNAUTHORIZED REQUEST");
                }
            }
            return true;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

        return false;

    }


}
