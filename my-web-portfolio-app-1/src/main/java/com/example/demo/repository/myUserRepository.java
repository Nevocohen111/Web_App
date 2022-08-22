package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface myUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT c FROM User c WHERE c.email = ?1")
    User findByEmail(String email);

    //set verificationtoken where string is the token
    @Modifying
    @Query(value = "UPDATE User SET verificationToken = ?1 WHERE email = ?2")
    void updateVerificationToken(String token, String email);

    @Query(value = "SELECT c FROM User c WHERE c.resetPasswordToken = ?1")
    User findByResetPasswordToken(String token);

    @Query(value = "SELECT c FROM User c WHERE c.id = ?1")
    List<User> findById(int id);

    User findByVerificationToken(String token);

    User findByUsername(String name);
}
