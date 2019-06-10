package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.Constants;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.managers.JsonManager;
import com.esliceu.keep_it_safe.managers.TokenManager;
import com.esliceu.keep_it_safe.managers.entities.UserManager;
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
import java.util.ArrayList;
import java.util.List;


@RestController
public class LoginController {

    private String clientId = "637594007727-3o8tnk0vhafhh7o0p3hk5tib3q5rudk6.apps.googleusercontent.com";

    private String secretId = "kgEDC-HtqsjxQZi6Jv-kYivr";

    @Value("${jwt.key}")
    private String SECRET_KEY;

    private UserRepository userRepository;
    private TokenManager tokenManager;
    private OAuth2ConnectionFactory<Google> factory = new GoogleConnectionFactory(clientId, secretId);
    private JsonManager jsonManager;
    private UserManager userManager;

    public static List<String> blackListToken = new ArrayList<>();

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
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    @RequestMapping(value = "/token/verify", method = RequestMethod.POST)
    public ResponseEntity<String[]> verifiedToken(@RequestBody String token, HttpServletResponse response) {

        // El token que recibe aqui esta mal formado
        System.out.println("This is de token Received: " + token);

        String jwt[] = tokenManager.validateToken(token);

        if(jwt != null) {
            System.out.println("This is de token I send: " + jwt[0]+ ", Token = "+ jwt[1]);
            response.addHeader("Access-Control-Allow-Credentials", "true");
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } else return new ResponseEntity(HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value = "/logOut", method = RequestMethod.POST)
    public void logOut(@RequestBody String token) {
        System.out.println("Token to purge HAHAHA -> " + token );
        LoginController.blackListToken.add(token);
        System.out.println("THE BLACK LIST " + LoginController.blackListToken.toString());
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