package com.example.demo.controllers;

import com.example.demo.model.Profile;
import com.example.demo.services.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@Slf4j
public class ProfileController {
    @Autowired
    private ProfileService service;


    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) throws InterruptedException {
        Profile profile = new Profile();
        profile.setName(authentication.getName());
        model.addAttribute("list", service.getAllCurrentUserProfiles(authentication.getName()));
        if(authentication.getName().equals("Nevo"))
            model.addAttribute("profileImg", service.getNevoImgUrl());
        else
            model.addAttribute("profileImg", service.getGuestImgUrl());
        if (service.getAllCurrentUserProfiles(authentication.getName()).size() == 0) {
            model.addAttribute("newProfile", true);
        }
        model.addAttribute("profile", profile);
        return "profile.html";
    }


    @PostMapping("/newContact")
    public String newContact(@ModelAttribute("profile") Profile profile) {
        List<Profile> list = service.getAllProfiles();
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(profile.getName()))
                service.updateProfile(list.get(i).getId(),profile.getName(),profile.getUserName(), profile.getLastName(), profile.getEmail(), profile.getHobbies(), profile.getWorkPlace(), profile.getPhone());
            else
                service.createProfile(profile,profile.getName(),profile.getUserName(), profile.getLastName(), profile.getEmail(), profile.getHobbies(), profile.getWorkPlace(), profile.getPhone());
        }
        return "redirect:/profile";
    }

/*    @RequestMapping(method = RequestMethod.POST, value = "/createImage")
    public String createFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        //Requested Parameter file is the name of the file input in the form in the current situation.
        try {
            if(!Objects.requireNonNull(file.getContentType()).equals("application/pdf")) {
                service.createImage(file);
            } else {
                redirectAttributes.addFlashAttribute("message", "Can only upload an image!");
            }
        } catch (IOException | InterruptedException e) {
            log.info("No file uploaded");
            redirectAttributes.addFlashAttribute("message", "Insert a picture first.");
        }
        return "redirect:/profile";

    }*/
}