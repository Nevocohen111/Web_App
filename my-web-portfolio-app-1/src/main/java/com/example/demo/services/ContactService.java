package com.example.demo.services;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.rest.RestExceptionHandler;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j

public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void saveMessageDetails(Contact contact) {
        contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public List<Contact> getAllOpenContacts() {
        return contactRepository.getAllOpenContacts();
    }

    public Optional<Contact> changeInquiryStateToClosed(Integer id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            contact.get().setState("close");
            contactRepository.save(contact.get());
        }
        return contact;
    }

    public JsonObject showRates(String from, String to, double amount) throws RestExceptionHandler.InvalidCurrencyException {
        JsonObject jsonObject = new JsonObject();
        if (from.equalsIgnoreCase("USD") && to.equalsIgnoreCase("ILS"))
            jsonObject.addProperty("rate", amount * 3.5);
        else if (from.equalsIgnoreCase("ILS") && to.equalsIgnoreCase("USD"))
            jsonObject.addProperty("rate", amount / 3.5);
        else if (from.equalsIgnoreCase("USD") && to.equalsIgnoreCase("USD"))
            jsonObject.addProperty("rate", amount);
        else if (from.equalsIgnoreCase("ILS") && to.equalsIgnoreCase("ILS"))
            jsonObject.addProperty("rate", amount);
        else
            throw new RestExceptionHandler.InvalidCurrencyException("Invalid currency");
        return jsonObject;
    }


    public List<Contact> fetchAllContactsByKeyword(String keyword) {
        return contactRepository.getAllContactsByKeyword(keyword);
    }

    public List<Contact> getAllOpenContactsByKeyword(String keyword) {
        return contactRepository.getAllOpenContactsByKeyword(keyword);
    }

    public Optional<Contact> changeInquiryStateToOpened(Integer id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            contact.get().setState("open");
            contactRepository.save(contact.get());
        }
        return contact;
    }

    public boolean deleteById(int id) {
        contactRepository.deleteById(id);
        return true;
    }

    public void deleteByKeyword(String name) {
        List<Contact> contact = contactRepository.findByName(name);
        contactRepository.deleteAll(contact);
    }
}


