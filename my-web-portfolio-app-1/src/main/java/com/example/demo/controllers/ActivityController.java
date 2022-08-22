package com.example.demo.controllers;

import com.example.demo.model.Activity;
import com.example.demo.services.ActivityService;;
import groovyjarjarantlr4.v4.codegen.model.ModelElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@SessionScope
@Slf4j
public class ActivityController {
    @Autowired
    private ActivityService service;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchBar(String keyword, Model model,Authentication authentication) {
        Activity activity = new Activity();
        activity.setProductPrice(authentication.getName());
        model.addAttribute("myPurchase", activity);
        if (keyword != null) {
            List<Activity> purchases = service.getByKeyword(keyword,authentication.getName());
            model.addAttribute("list", purchases);
            return "search";
        }
        else {
            List<Activity> list = service.getAllPurchases(authentication.getName());
            model.addAttribute("list", list);
            return "search.html";
        }

    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("myPurchase") Activity myPurchase, Errors errors, Model model, RedirectAttributes redirectAttributes,Authentication authentication) {
        if (errors.hasErrors()) {
            List<Activity> list = service.getAllPurchases(authentication.getName());
            model.addAttribute("list", list);
            return "search.html";
        }
        service.addPurchases(myPurchase);
        redirectAttributes.addFlashAttribute("message", "Activity was added");
        return "redirect:/search";
    }



    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset() {
        return "redirect:/search";
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(Model model, @PathVariable("id") Integer id,Authentication authentication) {
        service.deletePurchase(id);
        List<Activity> list = service.getAllPurchases(authentication.getName());
        model.addAttribute("list", list);
        return new ModelAndView("redirect:/search");
    }


}
