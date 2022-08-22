package com.example.demo.controllers;

import com.example.demo.model.Activity;
import com.example.demo.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EditController {

    @Autowired
    private ActivityService service;

    //present the page according to the id given in the url.
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Activity activity = service.searchById(id);
        model.addAttribute("myPurchase", activity);
        model.addAttribute("list", service.searchById(id));
        return "edit";
    }

    @PostMapping("/update")
    public String updateById( @Valid @ModelAttribute("myPurchase") Activity myPurchase, Errors errors,@RequestParam("id") Integer id, @RequestParam("ownerName") String ownerName, @RequestParam("ownerLastName") String ownerLastName, @RequestParam("ownerEmail") String ownerEmail, @RequestParam("productName") String productName, @RequestParam("productDescription") String productDescription, @RequestParam("userName") String userName, Model model, RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            model.addAttribute("list", service.searchById(id));
            //if an error occured in the email section
            if(errors.getFieldError("ownerEmail") != null)
                redirectAttributes.addFlashAttribute("message", "The urgency must be either high, medium or low!");
            else if(errors.getFieldError("userName") != null)
                redirectAttributes.addFlashAttribute("message", "Name is longer than 10 characters!");
            else if(errors.getFieldError("productName") != null)
                redirectAttributes.addFlashAttribute("message", "Time should be in the format of hh:mm");
            else if(errors.getFieldError("productDescription") != null)
                redirectAttributes.addFlashAttribute("message", "Description is not valid");
            else if(errors.getFieldError("ownerName") != null)
                redirectAttributes.addFlashAttribute("message", "The subject is not valid!");
            else if(errors.getFieldError("ownerLastName") != null)
                redirectAttributes.addFlashAttribute("message", "Time should be in the format of hh:mm");

            return "redirect:/edit/" + id;
        }
            service.updateMyActivity(id, ownerName, ownerLastName, ownerEmail, productName, productDescription, userName);
        redirectAttributes.addFlashAttribute("message", "Activity was updated");
        return "redirect:/search";
    }
}
