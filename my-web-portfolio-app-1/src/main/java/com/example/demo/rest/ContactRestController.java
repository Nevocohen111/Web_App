package com.example.demo.rest;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;
import com.example.demo.services.ContactService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/inquiries")
@CrossOrigin(origins = "*")
public class ContactRestController {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;
    //read
    @GetMapping("/getInquiriesByState")
    public List<Contact> getInquiriesByState(@RequestParam String state) {
        return contactRepository.findInquiryByState(state);
    }
    //create
    @PostMapping("/saveContact")
    public ResponseEntity<Contact> saveContact(@Valid @RequestBody Contact contact) {
        Contact response = contactRepository.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("isInquirySaved", "true").body(response);
    }

    @PostMapping("/rates")
    public JsonObject showRates(@RequestParam String from, @RequestParam String to, @RequestParam double amount) throws RestExceptionHandler.InvalidCurrencyException {
        return contactService.showRates(from, to, amount);
    }
   //delete
    @DeleteMapping("/deleteContact")
    public ResponseEntity<CustomHttpResponse> deleteContact(RequestEntity<Contact> request) {
         Contact contact = request.getBody();
         assert contact != null;
         contactRepository.deleteById(contact.getId());
         return ResponseEntity.status(HttpStatus.OK)

         .body(new CustomHttpResponse(200, "Inquiry deleted successfully"));
    }
    //update
    @Transactional
    @PutMapping(value = "/updateContact")
    public ResponseEntity<Contact> updateContact(@Valid @RequestBody Contact contact) {
        int id = contact.getId();
        if(id != -1) {
            contactRepository.updateContact(contact.getName(),contact.getUserName(),contact.getMobile_num(), contact.getEmail(), contact.getSubject(), contact.getMessage(), contact.getState(),contact.getId());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("isInquiryUpdated", "true").body(contact);
    }


}
