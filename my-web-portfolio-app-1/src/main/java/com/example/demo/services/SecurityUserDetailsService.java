package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repository.myUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    private myUserRepository myUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return myUserRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
    }

    public void updateResetPasswordToken(String token,String email) {
        User user = myUserRepository.findByEmail(email);
        if(user != null) {
            user.setResetPasswordToken(token);
        } else {
            throw new UsernameNotFoundException("Couldn't find user with email: " + email);
        }
    }

    //get the current user and check if enabled field is true or false
    public boolean isUserEnabled(String name) {
        User user = myUserRepository.findByUsername(name);
        if(user != null) {
            return user.getEnabled().equals("true");
        } else {
            throw new UsernameNotFoundException("Couldn't find user with email: " + name);
        }
    }



    public User getByResetPasswordToken(String token) {
        return myUserRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(UserDetails user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        User myUser = (User) user;
        myUser.setPassword(encodedPassword);
        myUser.setResetPasswordToken(null);
        myUserRepository.save(myUser);
    }


    //return true if both username and password in db match
    public boolean isValidUser(String username, String password) {
        List<User> users = myUserRepository.findAll();
        for(User user : users) {
            //if the encoded password matches the password in the db, return true
            if(user.getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    //find if username is present in db
    public boolean isUserPresent(String username) {
        return myUserRepository.findUserByUsername(username).isPresent();
    }

    //get user by role

    //find if email is present in db
    public boolean isEmailPresent(String email) {
        return myUserRepository.findUserByEmail(email).isPresent();
    }
    public void createUser(User user) {
        myUserRepository.save(user);
    }

    public void updateVerificationToken(String token,String email) {
         myUserRepository.updateVerificationToken(token,email);
    }

    public boolean deleteById(int id) {
        myUserRepository.deleteById(id);
        return true;
    }


    public List<User> getAllUsers() {
        return myUserRepository.findAll();
    }
    public List<User> getTheCurrentUserById(int id) {
        return myUserRepository.findById(id);
    }


    public User getUserByToken(String token) {
        return myUserRepository.findByVerificationToken(token);
    }


}
