package com.esliceu.keep_it_safe.managers;

import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        return user;
    }

    public void saveUser(User user) throws Exception {
      try {
         userRepository.save(user);
      } catch (RuntimeException e) {
            throw new Exception(e);
      }
    }
}
