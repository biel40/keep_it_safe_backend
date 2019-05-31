package com.esliceu.keep_it_safe.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;


@Controller
public class SocialFacebookController {

    private String clientId = "288489852104379";

    private String secretId = "5f87ae1634aef5053aa8f7c81ee72c91";

    private FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(clientId, secretId);

    @RequestMapping(value = "/loginFacebook", method = RequestMethod.POST)
    public String useAppFacebook() {

        OAuth2Operations operations = facebookConnectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();

        params.setRedirectUri("http://localhost:8081/forwardLoginFacebook");
        params.setScope("email, public_profile");

        String url = operations.buildAuthenticateUrl(params);

        System.out.println(url);

        return url;

    }

    @RequestMapping(value = "/forwardLoginFacebook", method = RequestMethod.GET)
    public RedirectView hola (@RequestParam("code")
                                String authorizationCode ,HttpServletResponse response) {


        OAuth2Operations operations = facebookConnectionFactory.getOAuthOperations();
        AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, "http://localhost:8081/forwardLoginFacebook", null);

        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessToken);

        Facebook facebookConnection = connection.getApi();

        System.out.println(accessToken.getAccessToken());

        if (facebookConnection.isAuthorized()) {
            response.setHeader("Authorisation","Bearer " + accessToken.getAccessToken());
            RedirectView redirectView = new RedirectView("http://localhost:8080");
            redirectView.setPropagateQueryParams(true);

            return redirectView;
        } else {
            return new RedirectView("http://localhost:8080");
        }

    }


}
