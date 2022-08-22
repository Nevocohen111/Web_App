package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.services.ProfileService;
import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CertificateController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @GetMapping("/certificates")
    public String displayCertificatesPage(Model model, Authentication authentication) {
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

        return "certificates";
    }
}
