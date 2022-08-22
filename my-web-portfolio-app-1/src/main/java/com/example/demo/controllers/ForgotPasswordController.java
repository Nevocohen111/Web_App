package com.example.demo.controllers;

import com.example.demo.mailUtility.Utility;
import com.example.demo.model.User;
import com.example.demo.services.SecurityUserDetailsService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;
    //java mailSender object(to send mail,Added in the pomXML)
    @Autowired
    private SecurityUserDetailsService userService;

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password.html";
    }


    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        //getting the email parameter from the form and generating a randomString for the token.
        try {
            //if the email is registered in the db then it will set the token column in the db with the randomString.
            userService.updateResetPasswordToken(token, email);
            //sending the email with the token.
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset-password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", "Email not found");
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
        return "forgot-password.html";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        //getting the token parameter from the url and setting it to the model.
        User user = userService.getByResetPasswordToken(token);
        //the model is being used by a 'hidden' input in the form. in order to store it each time the form is submitted.
        model.addAttribute("token", token);
        if (user == null) {
            //if the user is null it means that either the token is invalid or the user has already reset his password.
            model.addAttribute("message", "An error has occurred.\nPlease try again later.");
        }
        return "reset-password.html";

    }

    @PostMapping("/reset-password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        //getting the parameters from the form.
        User user = userService.getByResetPasswordToken(token);
        if (user == null)
            model.addAttribute("message", "Password has already changed");
        else {
            //if the user is found by the token then it will set the password column in the db with the password and reset the token for security issues
            userService.updatePassword(user, password);
            return "redirect:/login?reset=true";
        }
        return "reset-password.html";

    }


    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("support@Hotmail.com", "Portfolio Support");
        helper.setTo(recipientEmail);
        String subject = "Reset Password";
        String content = "<p>!Hello</p>" +
                "<p>.You have requested to reset your password</p>" +
                "<p>.Please click on the link below to reset your password</p>" +
                "<p><a href='" + link + "'>Reset Password</a></p>" +
                "<br>" +
                "<p>.Ignore this email if you do remember your password</p>" +
                "<p>!Thanks and have a wonderful day</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}