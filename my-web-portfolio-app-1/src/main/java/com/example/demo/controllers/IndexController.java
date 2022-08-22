package com.example.demo.controllers;


import com.example.demo.mailUtility.Utility;
import com.example.demo.model.Profile;
import com.example.demo.model.Reviews;
import com.example.demo.model.User;
import com.example.demo.services.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@SessionScope
@Slf4j
public class IndexController {
    private int count = 0;
    @Autowired
    private SecurityUserDetailsService userDetailsManager;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ReviewsService reviewsService;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = {"/", "/index"})
    public String displayIndexPage(Model model,Authentication authentication) {
        model.addAttribute("user", new User());

        if(authentication != null && authentication.getAuthorities() != null) {
            //if the name of the authenticated user is Nevo return the nevo img
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
        } else
            model.addAttribute("list", profileService.getNevoImgUrl());

        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String reset,@RequestParam(required = false) String registered, HttpServletRequest request, HttpSession session,Model model) {
        if(reset != null) {
            model.addAttribute("msg","Password has been changed successfully");
        }
        else if(registered != null) {
            model.addAttribute("msg","You have been registered successfully");
        }
        session.setAttribute("error", getErrorMessage(request));
        if(session.getAttribute("error") != null) {
            if(count > 10) {
                count = 0;
                throw new RuntimeException("Too many attempts!");
            }
            count++;
        }
        return "login.html";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") int userId,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Authentication authentication) {
        //delete the user by the given id(userId) the Request Parameter is presented in the html page.
        userDetailsManager.deleteById(userId);
        if(!profileService.findProfileByName(authentication.getName()).isEmpty())
            profileService.deleteByProfileName(authentication.getName());
        if(!activityService.findByProductPrice(authentication.getName()).isEmpty())
            activityService.deleteByProductPrice(authentication.getName());
        if(!contactService.fetchAllContactsByKeyword(authentication.getName()).isEmpty())
            contactService.deleteByKeyword(authentication.getName());
        if(!reviewsService.reviewsByName(authentication.getName()).isEmpty())
            reviewsService.deleteByName(authentication.getName());
        //returning to the logout page.
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            redirectAttributes.addFlashAttribute("message", auth.getName() + ",your account has been deleted successfully.");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping("/download_file")
    public void downloadFile(HttpServletResponse response) throws IOException {
        //Choosing the location of where the file currently is.
        File file = new File("src/main/resources/static/files/nevoResume.pdf");
        //setting the response headers.
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getName();
        response.setHeader(headerKey, headerValue);
        //the output stream is used to write the file to the response.
        ServletOutputStream outputStream = response.getOutputStream();
        //reading the file's content
        BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
        byte[] buffer = new byte[8192];
        int bytesRead;
        while((bytesRead = inputStream.read(buffer)) != -1) {
            //writing it to the OutputStream.
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String addUser(@RequestParam Map<String, String> body, @Valid @ModelAttribute("user") User user, Errors errors, HttpSession session, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        if (errors.hasErrors()) {
            model.addAttribute("problem", true);
            session.setAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            log.info("Error: " + errors.getAllErrors().get(0).getDefaultMessage());
            return "register";

        } else if (userDetailsManager.isUserPresent(user.getUsername())) {
            model.addAttribute("problem", true);
            session.setAttribute("error", "User name already exists");
            log.info("Error: " + "User name already exists");
            return "register";

        } else if (userDetailsManager.isEmailPresent(body.get("email"))) {
            model.addAttribute("problem", true);
            session.setAttribute("error", "Email already exists");
            log.info("Error: " + "Email already exists");
            return "register";
        } else {
            User myUser = new User();
            myUser.setUsername(body.get("username"));
            myUser.setPassword(passwordEncoder.encode(body.get("password")));
            myUser.setEmail(body.get("email"));
            myUser.setVerificationToken(token);
            myUser.setEnabled("false");
            ArrayList<GrantedAuthority> roleUser = new ArrayList<>();
            roleUser.add((GrantedAuthority) () -> "ROLE_USER");
            //setting the user's role to "ROLE_USER" like it's already defined by our CustomAuthenticationProvider.
            roleUser.forEach(authority -> myUser.setRole(authority.getAuthority()));
            userDetailsManager.updateVerificationToken(myUser.getEmail(), token);
            String activate = Utility.getSiteURL(request) + "/registerSuccess?token=" + token;
            sendEmail(email, activate);
            userDetailsManager.createUser(myUser);
            model.addAttribute("emailRegistered", myUser.getUsername() + ", check your email to verify your account.");
            return "register";
        }
    }

    @GetMapping("/registerSuccess")
    public String registerSuccess(@Param(value = "token") String token,Model model) {
        User user = userDetailsManager.getUserByToken(token);
        if(user == null) {
            model.addAttribute("message","an error has occurred");
        }
        return "registerSuccess.html";
    }

    @PostMapping("/registerSuccess")
    public String activate(@RequestParam("token") String token,Model model) {
        User user = userDetailsManager.getUserByToken(token);
        if(user == null) {
            model.addAttribute("message","an error has occurred");
            return "registerSuccess.html";
        }
        user.setEnabled("true");
        userDetailsManager.createUser(user);
        return "redirect:/login?registered=true";
    }

    private String getErrorMessage(HttpServletRequest request) {
        //handling exceptions and count for presenting a custom error page via ExceptionController.
        Exception exception = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        String error;
        if (exception instanceof BadCredentialsException) {
            error = "credentials does not match!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else if (exception instanceof UsernameNotFoundException) {
            error = "Credentials are not correct!";
        } else {
            error = "Credentials are not valid!";
        }
        return error;
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        //create user only when the user clicks the link in the email.
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("support@Hotmail.com", "Portfolio Support");
        helper.setTo(recipientEmail);
        String subject = "Activate account";
        String content = "<p>!Hello</p>" +
                "<p>.In order to log in please click the link below</p>" +
                "<p><a href='" + link + "'>Activate account</a></p>" +
                "<br>" +
                "<p>!Thanks and have a wonderful day</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }



}
