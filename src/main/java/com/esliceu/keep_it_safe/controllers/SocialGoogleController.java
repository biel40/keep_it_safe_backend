package com.esliceu.keep_it_safe.controllers;

import org.springframework.social.connect.Connection;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletResponse;


@RestController
public class SocialGoogleController {

    private String clientId = "654562017313-c2j3vnt9i253slup3f2mtmnoeutj4jdd.apps.googleusercontent.com";

    private String secretId = "iP00UFVzF52QSOitDauiRvDS";

    private GoogleConnectionFactory factory = new GoogleConnectionFactory(clientId, secretId);

    @RequestMapping( value = "/loginGoogle", method = RequestMethod.POST)
    public String useAppGoogle() {

        OAuth2Operations operations = factory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();

        params.setRedirectUri("http://localhost:8081/forwardLoginGoogle");
        params.setScope("email profile openid");

        String url = operations.buildAuthenticateUrl(params);

        System.out.println("The URL is: " + url);

        return url;

    }

    @RequestMapping(value = "/forwardLoginGoogle", method = RequestMethod.GET )
    public RedirectView forward(@RequestParam("code")
                                String authorizationCode, HttpServletResponse response) {



        OAuth2Operations operations = factory.getOAuthOperations();

        
        AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, "http://localhost:8081/forwardLoginGoogle", null);
        Connection<Google> connection = factory.createConnection(accessToken);

        Google googleConnection = connection.getApi();

        System.out.println(googleConnection.getAccessToken());

        if (googleConnection.isAuthorized()) {
            response.setHeader("hola", googleConnection.getAccessToken());

            RedirectView redirectView = new RedirectView("http://localhost:8080");
            redirectView.setPropagateQueryParams(true);
            return redirectView;
        } else {
            return new RedirectView("http://localhost:8080");
        }


    }

}
