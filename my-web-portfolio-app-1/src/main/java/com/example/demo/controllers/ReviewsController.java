package com.example.demo.controllers;

import com.example.demo.model.Reviews;
import com.example.demo.model.User;
import com.example.demo.services.ProfileService;
import com.example.demo.services.ReviewsService;
import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;

    @Autowired
    private ProfileService profileService;


    @GetMapping("/reviews")
    public String reviews(Model model, Authentication authentication) {
        Reviews reviews = new Reviews();
        if (authentication != null && authentication.getAuthorities() != null) {
            reviews.setName(authentication.getName());
            model.addAttribute("lists", reviewsService.reviewsByName(authentication.getName()));
            model.addAttribute("reviews", reviews);
            if (reviewsService.reviewsByName(authentication.getName()).size() == 0) {
                model.addAttribute("newReview", true);
            }
            if (authentication.getName().equals("Nevo")) {
                model.addAttribute("list", profileService.getNevoImgUrl());
            } else {
                model.addAttribute("list", profileService.getGuestImgUrl());
            }

            List<User> users = userDetailsManager.getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(authentication.getName())) {
                    model.addAttribute("myUser", userDetailsManager.getTheCurrentUserById(user.getId()));
                }
            }
        } else {
            model.addAttribute("reviews", reviews);
        }
        return "reviews";
    }

    @PostMapping("/createReview")
    public String createReview(@ModelAttribute("reviews") Reviews reviews) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(reviews.getName())) {
                reviewsService.updateReview(list.get(i).getId(), reviews.getUserName(), reviews.getTitle(), reviews.getSubject(), date);
            } else {
                reviewsService.createReview(reviews, reviews.getName(), reviews.getUserName(), reviews.getTitle(), reviews.getSubject(), date);
            }
        }
        return "redirect:/reviews";
    }

    @GetMapping("/clearReview")
    public String clearReview() {
        String userName = "Name";
        String title = "Title";
        String subject = "Subject";
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            reviewsService.updateReview(list.get(i).getId(), userName, title, subject, date);
        }
        return "redirect:/reviews";
    }

    @GetMapping("/updateStar1")
    public String changeStarOneColor(Authentication authentication) {
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(authentication.getName())) {
                String star1 = reviewsService.getStar1(list.get(i).getId());
                star1 = star1.equals("black") ? "blue" : "black";
                reviewsService.updateStar1(list.get(i).getId(), star1);
            }
        }
        return "redirect:/reviews";
    }

    @GetMapping("/updateStar2")
    public String changeStarTwoColor(Authentication authentication) {
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(authentication.getName())) {
                String star2 = reviewsService.getStar2(list.get(i).getId());
                star2 = star2.equals("black") ? "blue" : "black";
                reviewsService.updateStar2(list.get(i).getId(), star2);
            }
        }
        return "redirect:/reviews";
    }


    @GetMapping("/updateStar3")
    public String changeStarThreeColor(Authentication authentication) {
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(authentication.getName())) {
                String star3 = reviewsService.getStar3(list.get(i).getId());
                star3 = star3.equals("black") ? "blue" : "black";
                reviewsService.updateStar3(list.get(i).getId(), star3);
            }
        }
        return "redirect:/reviews";
    }

    @GetMapping("/updateStar4")
    public String changeStarFourColor(Authentication authentication) {
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(authentication.getName())) {
                String star4 = reviewsService.getStar4(list.get(i).getId());
                star4 = star4.equals("black") ? "blue" : "black";
                reviewsService.updateStar4(list.get(i).getId(), star4);
            }
        }
        return "redirect:/reviews";
    }

    @GetMapping("/updateStar5")
    public String changeStarFiveColor(Authentication authentication) {
        List<Reviews> list = reviewsService.showAllReviews();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(authentication.getName())) {
                String star5 = reviewsService.getStar5(list.get(i).getId());
                star5 = star5.equals("black") ? "blue" : "black";
                reviewsService.updateStar5(list.get(i).getId(), star5);
            }
        }
        return "redirect:/reviews";
    }
}
