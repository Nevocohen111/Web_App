package com.example.demo.repository;

import com.example.demo.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Modifying
    @Query(value = "UPDATE profile SET name = :name, last_name = :lastName, email = :email, hobbies = :hobbies, work_place = :workPlace, phone = :phone WHERE id = :id", nativeQuery = true)
    void updateProfile(@Param("id") int id, @Param("name") String name, @Param("lastName") String lastName, @Param("email") String email, @Param("hobbies") String hobbies, @Param("workPlace") String workPlace, @Param("phone") String phone);
    @Modifying
    @Query(value = "UPDATE profile SET name = :name WHERE id = :id", nativeQuery = true)
    void updateProfileName(@Param("id") int id, @Param("name") String name);

    @Modifying
    @Query(value = "UPDATE profile SET imgurl = :imgURL WHERE id = :id", nativeQuery = true)
    void createImage(@Param("id") int id, @Param("imgURL") String imgURL);

    @Query(value = "SELECT * FROM profile WHERE name LIKE %:name%", nativeQuery = true)
    List<Profile> findProfileByName(@Param("name") String name);

    Profile findByName(String name);
    @Query(value = "SELECT * FROM profile WHERE name LIKE %:name%", nativeQuery = true)
    List<Profile> findAllByName(String name);

    List<Profile> findProfileById(int userId);


    //add only name section alone in a new record if it is not already present in the database
}
