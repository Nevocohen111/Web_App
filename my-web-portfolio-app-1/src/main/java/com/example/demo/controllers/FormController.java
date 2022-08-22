package com.example.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@CrossOrigin
public class FormController {
    @GetMapping("/look")
    public String form(String key, Authentication authentication) {
        String[] keyParts = key.split(" ");
        if (authentication != null) {
            for (int i = 0; i < keyParts.length; i++) {
                keyParts[i] = keyParts[i].toLowerCase();
                if (keyParts[i].contains("act")) {
                    return "redirect:/search";
                } else if (keyParts[i].contains("cer")) {
                    return "redirect:/certificates";
                } else if(keyParts[i].contains("cur")) {
                    return "redirect:/convert";
                }else if (keyParts[i].contains("hol")) {
                    return "redirect:/holidays";
                }else if(keyParts[i].contains("pro")) {
                    return "redirect:/profile";
                }else if(keyParts[i].contains("rev")) {
                    return "redirect:/reviews";
                }else if(keyParts[i].contains("con")) {
                    return "redirect:/contact";
                } else if (keyParts[i].contains("inq")) {
                    return "redirect:/inquiries";
                } else if (keyParts[i].contains("pla")) {
                    return "redirect:/carousel";
                }
            }
        } else {
            for (int i = 0; i < keyParts.length; i++) {
                keyParts[i] = keyParts[i].toLowerCase();
                if (keyParts[i].contains("reg")) {
                    return "redirect:/register";
                } else if (keyParts[i].contains("cer")) {
                    return "redirect:/certificates";
                } else if (keyParts[i].contains("log")) {
                    return "redirect:/login";
                } else if (keyParts[i].contains("for")) {
                    return "redirect:/forgot-password";
                } else if (keyParts[i].contains("hol")) {
                    return "redirect:/holidays";
                } else if (keyParts[i].contains("inq")) {
                    return "redirect:/inquiries";
                }else if(keyParts[i].contains("pro")) {
                    return "redirect:/profile";
                }else if(keyParts[i].contains("rev")) {
                    return "redirect:/reviews";
                }else if (keyParts[i].contains("act")) {
                    return "redirect:/search";
                }else if(keyParts[i].contains("con")) {
                    return "redirect:/contact";
                } else if (keyParts[i].contains("pla")) {
                    return "redirect:/carousel";
                }else if(keyParts[i].contains("cur")) {
                    return "redirect:/convert";
                }
            }
        }
        return "redirect:/";
    }
}
