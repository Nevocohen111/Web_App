package com.example.demo.repository;

import com.example.demo.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query(value = "SELECT * FROM contact WHERE state = 'open'", nativeQuery = true)
    List<Contact> getAllOpenContacts();
    //get all users by keyword.
    @Query(value = "SELECT * FROM contact WHERE state = 'open' AND (name like %:keyword%)", nativeQuery = true)
    List<Contact> getAllOpenContactsByKeyword(@Param("keyword") String keyword);
    @Query(value = "SELECT * FROM contact WHERE name like %:keyword%", nativeQuery = true)
    List<Contact> getAllContactsByKeyword(@Param("keyword") String keyword);

    List<Contact> findByName(String name);
    @Query("select c from Contact c where c.state = ?1")
    List<Contact> findInquiryByState(String state);
    @Modifying
    @Query(value = "UPDATE contact SET name = :name, user_name = :userName,mobile_num = :mobile_num, email = :email, subject = :subject, message = :message, state = :state WHERE id = :id", nativeQuery = true)
    void updateContact(@Param("name") String name, @Param("userName") String userName, @Param("mobile_num") String mobile_num, @Param("email") String email, @Param("subject") String subject, @Param("message") String message, @Param("state") String state,@Param("id") int id);
}
