package com.esliceu.keep_it_safe.controller;

import com.esliceu.keep_it_safe.Constants;
import com.esliceu.keep_it_safe.entity.User;
import com.esliceu.keep_it_safe.manager.JsonManager;
import com.esliceu.keep_it_safe.manager.TokenManager;
import com.esliceu.keep_it_safe.manager.entity.UserManager;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${jwt.key}")
    private String SECRET_KEY;

    private UserRepository userRepository;
    private TokenManager tokenManager;
    private OAuth2ConnectionFactory<Google> factory = new GoogleConnectionFactory(Constants.GOOGLE_CLIENT_ID, Constants.GOOGLE_SECRET_ID);
    private JsonManager jsonManager;
    private UserManager userManager;


    @Autowired
    public LoginController(UserRepository userRepository, TokenManager tokenManager, JsonManager jsonManager, UserManager userManager) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
        this.jsonManager = jsonManager;
        this.userManager = userManager;
    }


    @RequestMapping(value = "/login/local", method = RequestMethod.POST)
    public ResponseEntity<String> localLogin(@RequestBody String jsonLogin) {

        // Deserializamos el JSON que nos llega por el Body y lo convertimos a un objeto User.
        User user = jsonManager.userFromLocal(jsonLogin);
        User userInDb = userManager.getUserByEmailAndPassword(user.getEmail(), user.getPassword());

        // Si no existe, devolvemos un Unauthorized
        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // Si existe le generamos un Token
        return new ResponseEntity<>(tokenManager.getJWTToken(userInDb), HttpStatus.OK);

    }

    @RequestMapping(value = "/oAuth/google", method = RequestMethod.POST)
    public String useAppGoogle() {

        OAuth2Operations operations = factory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();

        params.setRedirectUri(Constants.GOOGLE_FORWARDING_URL);
        params.setScope("email profile openid");

        String url = operations.buildAuthenticateUrl(params);

        return url;
    }

    @RequestMapping(value = "/forwardLoginGoogle", method = RequestMethod.GET)
    public void forward(@RequestParam("code")
                        String authorizationCode, HttpServletResponse response, HttpServletRequest request) throws Exception {

        OAuth2Operations operations = factory.getOAuthOperations();
        AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, Constants.GOOGLE_FORWARDING_URL, null);

        String verifiedToken = this.verified(Constants.GOOGLE_VERIFICATIONTOKEN_URL + accessToken.getAccessToken());
        String jwt;

        if (verifiedToken != null) {

            User user = jsonManager.userFromGoogleJson(verifiedToken);
            User userInDB = userRepository.findByEmail(user.getEmail());

            if (userInDB != null) {
                jwt = tokenManager.getJWTToken(userInDB);
            } else {
                userManager.saveUser(user);
                jwt = tokenManager.getJWTToken(user);
            }

            response.sendRedirect( Constants.FRONTEND_URL + "?token=" + jwt);

        } else {
            response.sendRedirect(Constants.FRONTEND_URL);
        }
    }

    @RequestMapping(value = "/token/verify", method = RequestMethod.POST)
    public ResponseEntity<String[]> verifiedToken(@RequestBody String token) {

        String jwt[] = tokenManager.validateToken(token);

        if(jwt != null) {
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } else return new ResponseEntity(HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value = "/logOut", method = RequestMethod.POST)
    public void logOut(@RequestBody String token) {
        TokenManager.blackListToken.add(token);
    }

    private String verified(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        if (urlConnection.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            urlConnection.disconnect();
            return content.toString();
        }
        urlConnection.disconnect();
        return null;
    }

}