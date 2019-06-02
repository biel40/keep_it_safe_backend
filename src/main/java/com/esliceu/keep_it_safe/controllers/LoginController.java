package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.JsonController;
import com.esliceu.keep_it_safe.TokenController;
import com.esliceu.keep_it_safe.UserController;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@RestController
public class LoginController {

    private String clientId = "637594007727-3o8tnk0vhafhh7o0p3hk5tib3q5rudk6.apps.googleusercontent.com";

    private String secretId = "kgEDC-HtqsjxQZi6Jv-kYivr";

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Value("${verify.token.google}")
    private String verifyUrlGoogleToken;

    private UserRepository userRepository;
    private TokenController tokenController;
    private OAuth2ConnectionFactory factory = new GoogleConnectionFactory(clientId, secretId);
    private JsonController jsonController;
    private UserController userController;

    @Autowired
    public LoginController(UserRepository userRepository, TokenController tokenController, JsonController jsonController, UserController userController){
        this.userRepository = userRepository;
        this.tokenController = tokenController;
        this.jsonController = jsonController;
        this.userController = userController;
    }


    @RequestMapping(value = "/localLogin", method = RequestMethod.POST)
    public ResponseEntity<String> localLogin(@RequestBody String jsonLogin) {

        // Deserializamos el JSON que nos llega por el Body y lo convertimos a un objeto User.
        User user = jsonController.userFromLocal(jsonLogin);
        User userInDb = userController.getUserByEmailAndPasswos(user.getEmail(), user.getPassword());

        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(tokenController.getJWTToken(userInDb), HttpStatus.OK);

    }

    @RequestMapping( value = "/oAuth/google", method = RequestMethod.POST)
    public String useAppGoogle() {

        OAuth2Operations operations = factory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();

        params.setRedirectUri("http://localhost:8081/forwardLoginGoogle");
        params.setScope("email profile");

        String url = operations.buildAuthenticateUrl(params);

        System.out.println("The URL is: " + url);
        return url;
    }

    @RequestMapping(value = "/forwardLoginGoogle", method = RequestMethod.GET )
    public void forward(@RequestParam("code")
                        String authorizationCode, HttpServletResponse response, HttpServletRequest request) throws Exception {

        OAuth2Operations operations = factory.getOAuthOperations();

        AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, "http://localhost:8081/forwardLoginGoogle", null);

        String verificationString = this.verified(verifyUrlGoogleToken + accessToken.getAccessToken());
        String jwtToken;


        if(verificationString != null){

            User user = jsonController.userFromGoogleJson(verificationString);
            User userInDB = userRepository.findByEmail(user.getEmail());

            if(userInDB != null) {
                jwtToken = tokenController.getJWTToken(userInDB);
            } else {
                userController.saveUser(user);
                jwtToken = tokenController.getJWTToken(user);
            }

            response.sendRedirect("http://localhost:8080?token=" + jwtToken);

        } else  {
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    private String verified(String foo) throws IOException {

        URL url = new URL(foo);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println(con.getResponseCode());

        if (con.getResponseCode() == 200){
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            con.disconnect();
            return  content.toString();
        }
        con.disconnect();
        return  null;
    }

}