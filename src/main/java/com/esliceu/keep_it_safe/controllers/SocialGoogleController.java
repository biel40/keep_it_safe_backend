package com.esliceu.keep_it_safe.controllers;

import org.springframework.social.connect.Connection;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController
public class SocialGoogleController {

    private String clientId = "654562017313-c2j3vnt9i253slup3f2mtmnoeutj4jdd.apps.googleusercontent.com";

    private String secretId = "iP00UFVzF52QSOitDauiRvDS";

    private GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(clientId, secretId);


    @GetMapping(value = "/useApp")
    public String useApp() {

        OAuth2Operations operations = googleConnectionFactory.getOAuthOperations();

        OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();

        oAuth2Parameters.setRedirectUri("http://localhost:8082/forwardLogin");
        oAuth2Parameters.setScope("email profile openid");

        String url = operations.buildAuthenticateUrl(oAuth2Parameters);

        System.out.println("The URL is: " + url);

        return "redirect:" + url;

    }

    @RequestMapping(value = "/forwardLogin")
    public RedirectView forward(@RequestParam("code")
                                        String authorizationCode) {

        /* Estudiar que hacen estas líneas */

        OAuth2Operations operations = googleConnectionFactory.getOAuthOperations();
        AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, "http://localhost:8082/forwardLogin", null);
        Connection<Google> connection = googleConnectionFactory.createConnection(accessToken);

        Google googleConnection = connection.getApi();

        // Checks if the Google Connection has been succesfully achieved.
        if (googleConnection != null) {
            return new RedirectView("http://localhost:8080/#/");
        } else {
            return new RedirectView("http://localhost:8082/useApp");
        }

    }

}
