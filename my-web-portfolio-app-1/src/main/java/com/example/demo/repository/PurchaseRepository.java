package com.example.demo.repository;

import com.example.demo.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Activity,Integer> {
    //select a specific product_price record where  owner_name like %:keyword% or product_name like %:keyword%"

    @Query(value = "SELECT * FROM purchase WHERE owner_name like %:keyword% and product_price like %:name%", nativeQuery = true)
    List<Activity> findByKeyword(@Param("keyword") String keyword,@Param("name") String name);
    @Query(value = "SELECT * FROM purchase WHERE product_price like %:keyword%", nativeQuery = true)
    List<Activity> findByName(@Param("keyword") String keyword);
    @Query(value = "UPDATE purchase SET owner_name = :ownerName, owner_last_name = :ownerLastName, owner_email = :ownerEmail, product_name = :productName, product_description = :productDescription, user_name = :userName WHERE id = :id", nativeQuery = true)
    @Modifying
    void update(@Param("id") Integer id, @Param("ownerName") String ownerName, @Param("ownerLastName") String ownerLastName, @Param("ownerEmail") String ownerEmail, @Param("productName") String productName, @Param("productDescription") String productDescription, @Param("userName") String userName);

    List<Activity> findByProductPrice(String productPrice);
}
