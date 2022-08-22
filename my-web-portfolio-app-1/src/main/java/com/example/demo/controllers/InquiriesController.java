package com.example.demo.controllers;

import com.example.demo.model.Contact;
import com.example.demo.model.User;
import com.example.demo.services.ContactService;
import com.example.demo.services.ProfileService;
import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class InquiriesController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @GetMapping("/inquiries")
    public String displayInquiriesPage(Model model, Authentication authentication) {
        Contact contact = new Contact();
        if(authentication != null && authentication.getAuthorities() != null) {
            if(authentication.getName().equals("Nevo"))
                model.addAttribute("list", profileService.getNevoImgUrl());
            else
                model.addAttribute("list", profileService.getGuestImgUrl());

            List<User> users =  userDetailsManager.getAllUsers();
            for(User user : users) {
                if (user.getUsername().equals(authentication.getName())) {
                    model.addAttribute("myUser", userDetailsManager.getTheCurrentUserById(user.getId()));
                }
            }
        }
        //splitting GrantedAuthority list in order to render two different messages to the admin and the user.
        if(authentication != null) {
            List<GrantedAuthority> list = (List<GrantedAuthority>) authentication.getAuthorities();
            String role = list.get(0).getAuthority();
            if(role.equals("ROLE_ADMIN")) {
                model.addAttribute("admin","Hello admin, browse users directory.");
                model.addAttribute("lists", contactService.getAllContacts());
            }else if(role.equals("ROLE_USER")) {
                model.addAttribute("name", authentication.getName());
                contact.setName(authentication.getName());
                model.addAttribute("lists", contactService.getAllOpenContactsByKeyword(authentication.getName()));
            }
        }else {
            model.addAttribute("admin","Hello guest, browse users directory.");
        }
        //rendering all the inquiries to the user/admin.

        return "inquiries.html";
    }

    @GetMapping("/closeInquiry")
    public String delete(@RequestParam("id") int id,Authentication authentication) {
        List<GrantedAuthority> authenticationAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        //again splitting the logic in order to render two different outcomes,an admin can close but not delete the existence of the message in the db.
        //while a user can delete his message entirely so not he and nor the admin can ever see it again.
        String role = authenticationAuthorities.get(0).getAuthority();
        if(role.equals("ROLE_ADMIN")) {
            contactService.changeInquiryStateToClosed(id);
        } //if the user has no role
        else {
            contactService.deleteById(id);
        }
        return "redirect:/inquiries";
    }

    @GetMapping("/deleteInquiry")
    public String deleteInquiryAsAdmin(@RequestParam("id") int id) {
        contactService.deleteById(id);
        return "redirect:/inquiries";
    }

    @GetMapping("/openInquiry")
    public String open(@RequestParam("id") int id,Authentication authentication) {
        List<GrantedAuthority> authenticationAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String role = authenticationAuthorities.get(0).getAuthority();
        if(role.equals("ROLE_ADMIN")) {
            contactService.changeInquiryStateToOpened(id);
        }
        return "redirect:/inquiries";
    }



}
