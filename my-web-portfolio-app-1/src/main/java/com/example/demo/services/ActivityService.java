package com.example.demo.services;

import com.example.demo.model.Activity;
import com.example.demo.repository.PurchaseRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ActivityService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    //get the list of purchases.
    public List<Activity> getAllPurchases(String name) {
        return purchaseRepository.findByName(name);
    }

    //get the list of purchases by keyword.
    public List<Activity> getByKeyword(String keyword,String name){
        return purchaseRepository.findByKeyword(keyword,name);
    }

    //update the purchase.
    public void updateMyActivity(int id, String ownerName, String ownerLastName, String ownerEmail, String userName, String productDescription, String productPrice) {
        Activity activity = purchaseRepository.findById(id).get();
        if(!ownerName.isEmpty())
            activity.setOwnerName(ownerName);
        if(!ownerLastName.isEmpty())
            activity.setOwnerLastName(ownerLastName);
        if(!ownerEmail.isEmpty())
            activity.setOwnerEmail(ownerEmail);
        if(!userName.isEmpty())
            activity.setUserName(userName);
        if(!productDescription.isEmpty())
            activity.setProductDescription(productDescription);
        purchaseRepository.update(id, ownerName, ownerLastName, ownerEmail, userName, productDescription, productPrice);
    }

    public Activity searchById(int id) {
        return purchaseRepository.findById(id).get();
    }

    //delete a purchase by id.
    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }
    //add a purchase
    public void addPurchases(Activity purchase) {
        purchaseRepository.save(purchase);
    }

    public List<Activity> findByProductPrice(String productPrice) {
        return purchaseRepository.findByProductPrice(productPrice);
    }

    public void deleteByProductPrice(String productPrice) {
        List<Activity> activity = purchaseRepository.findByName(productPrice);
        purchaseRepository.deleteAll(activity);
    }
}
