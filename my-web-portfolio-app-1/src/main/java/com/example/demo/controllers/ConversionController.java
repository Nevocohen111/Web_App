package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.services.ConversionApiService;
import com.example.demo.services.ProfileService;
import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ConversionController {
    @Autowired
    private ConversionApiService conversionApiService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @GetMapping("/convert")
    public String convert(Authentication authentication,Model model) {
        if (authentication != null && authentication.getAuthorities() != null) {
            if (authentication.getName().equals("Nevo"))
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
        return "convert";
    }

    @PostMapping("/rates")
    public String showConversion(@RequestParam(required = false,value = "from") String from, @RequestParam(required = false,value = "to") String to, @RequestParam(required = false,value = "amount") String amount, RedirectAttributes redirectAttributes, Model model) {
        String result = conversionApiService.convert(from, to, amount);
        if(result.equalsIgnoreCase("Invalid currency")) {
            redirectAttributes.addFlashAttribute("error", "Invalid currency");
        }
        else {
            redirectAttributes.addFlashAttribute("result", result);
        }
        return "redirect:/convert";
    }
}
