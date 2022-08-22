package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.rest.RestExceptionHandler;
import com.example.demo.services.ContactService;
import com.example.demo.model.Contact;
import com.example.demo.services.ProfileService;
import com.example.demo.services.SecurityUserDetailsService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @GetMapping("/contact")
    public String contact(Model model, Authentication authentication) {
        //fetching the img url from the database and pass it to the model.
        Contact contact = new Contact();
        if (authentication != null && authentication.getAuthorities() != null) {
            if(authentication.getName().equals("Nevo"))
                model.addAttribute("list", profileService.getNevoImgUrl());
            else
                model.addAttribute("list", profileService.getGuestImgUrl());

            contact.setName(authentication.getName());
            //if both authentication and authentication.getAuthorities are not null, then we can get the user from the database
            List<User> users = userDetailsManager.getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(authentication.getName())) {
                    //if the current user's name that is on the list equals the name of the user that is logged in.
                    model.addAttribute("myUser", userDetailsManager.getTheCurrentUserById(user.getId()));
                    //pass the specific user object that's logged in to the model(contactInfo page going to render the result and delete by its id).
                }
            }
            model.addAttribute("contact", contact);
        }else {
            contact.setName("Guest");
            model.addAttribute("contact", contact);
        }

        return "contact";
    }

    @PostMapping("/saveMsg")
    public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "contact";

        }
        contact.setState("open");
        contactService.saveMessageDetails(contact);
        redirectAttributes.addFlashAttribute("message", "Thanks, your inquiry has been submitted successfully!");
        log.info("incoming message from " + contact.getName());

        return "redirect:/contact";
    }

}
