package com.example.demo.repository;

import com.example.demo.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    @Query(value = "SELECT * FROM reviews WHERE name like %:name%", nativeQuery = true)
    List<Reviews> findMyReviews(String name);


    Reviews findByName(String name);

    List<Reviews> findAllByName(String name);
}
