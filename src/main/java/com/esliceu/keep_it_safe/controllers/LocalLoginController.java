package com.esliceu.keep_it_safe.controllers;

import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.UserRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
public class LocalLoginController {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    private UserRepository userRepository;

    @Autowired
    public LocalLoginController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/localLogin", method = RequestMethod.POST)
    public ResponseEntity<String> localLogin(@RequestBody String jsonLogin) {

        // Deserializamos el JSON que nos llega por el Body y lo convertimos a un objeto User.
        Gson gson = new Gson();
        User user = gson.fromJson(jsonLogin, User.class);

        User userInDb = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        // Si no existe, devolvemos un Unauthorized
        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Si existe le generamos un Token
        return new ResponseEntity<>(getJWTToken(userInDb), HttpStatus.OK);

    }

    private String getJWTToken(User user) {

        System.out.println(SECRET_KEY);
        System.out.println(user.toString());

        String token = Jwts
                .builder()
                .setSubject(user.toString())
                .claim("role", user.getRol_user())
                .claim("name", user.getName())
                .claim("firstSurname", user.getFirst_surname())
                .claim("secondSurname", user.getSecond_surname())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SECRET_KEY)).compact();

        return "Bearer " + token;

    }

}