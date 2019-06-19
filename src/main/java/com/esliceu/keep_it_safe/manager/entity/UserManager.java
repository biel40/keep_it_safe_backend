package com.esliceu.keep_it_safe.manager.entity;

import com.esliceu.keep_it_safe.entity.Invoice;
import com.esliceu.keep_it_safe.entity.User;
import com.esliceu.keep_it_safe.manager.TokenManager;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public String[] updateUser(User userToUpdate ) throws Exception{

        try {
            User user = userRepository.findAllByUserId(userToUpdate.getUserId());
            user.setName(userToUpdate.getName());
            user.setSurnames(userToUpdate.getSurnames());
            user.setEmail(userToUpdate.getEmail());
            userRepository.save(user);

            String token  = this.tokenManager.getJWTToken(user);

            return this.tokenManager.validateToken(token);
        } catch (RuntimeException e) {
            throw new Exception(e);
        }


    }

    public String[] updatePassword(String[] data) throws Exception{
        try {
            long idUser =  Long.parseLong(data[0]);
            String oldPassword = data[1];
            String newPassword =  data[2];

            User user = userRepository.findAllByUserId(idUser);

            if(user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);

                String token  = this.tokenManager.getJWTToken(user);

                return this.tokenManager.validateToken(token);
            } else {
                return  null;
            }
        } catch (RuntimeException e) {
            throw new Exception(e);
        }
    }
}
