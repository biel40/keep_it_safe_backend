package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.managers.TokenManager;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserManager {

    private UserRepository userRepository;
    private TokenManager tokenManager;
    @Autowired
    public UserManager(UserRepository userRepository, TokenManager tokenManager){
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    public User getUserByID(long userID) {
        User user = userRepository.findAllByUserId(userID);
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        return user;
    }

    public User getUserByInvoice(Invoice invoice){
        return userRepository.findByInvoices(invoice);
    }

    public void saveUser(User user) throws Exception {
      try {
         userRepository.save(user);
      } catch (RuntimeException e) {
            throw new Exception(e);
      }
    }

    public String[] updateUser(User userToUpdate ) {

        User user = userRepository.findAllByUserId(userToUpdate.getUserId());
        user.setName(userToUpdate.getName());
        user.setSurnames(userToUpdate.getSurnames());
        user.setEmail(userToUpdate.getEmail());
        userRepository.save(user);

        String token  = this.tokenManager.getJWTToken(user);

        return this.tokenManager.validateToken(token);
    }
}
