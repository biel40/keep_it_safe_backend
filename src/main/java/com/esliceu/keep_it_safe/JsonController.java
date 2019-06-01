package com.esliceu.keep_it_safe;

import com.esliceu.keep_it_safe.entities.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JsonController {

    private Gson gson;
    private ApplicationContext context;
    @Autowired
    public JsonController(Gson gson, ApplicationContext context){
        this.gson = gson;
        this.context = context;
    }


    public User userFromGoogleJson(String json) {
        JsonObject googleUser = gson.fromJson(json, JsonObject.class);
        String email = googleUser.get("emails").getAsJsonArray().get(0).getAsJsonObject().get("value").toString().replace("\"", "");
        String surnames =  googleUser.get("name").getAsJsonObject().get("familyName").toString().replace("\"", "");
        String name = googleUser.get("name").getAsJsonObject().get("givenName").toString().replace("\"", "");

        return this.createUser(email, name, surnames);
    }

    public User userFromLocal(String userJson) {
        User user = gson.fromJson(userJson, User.class);
        return  user;
    }


    private User createUser (String email, String name, String surnames) {
        User user = context.getBean(User.class);
        user.setEmail(email);
        user.setName(name);
        user.setSurnames(surnames);
        user.setRol_user(RolUser.CLIENT);

        return user;
    }
}
