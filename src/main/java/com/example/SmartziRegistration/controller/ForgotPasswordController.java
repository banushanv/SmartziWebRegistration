package com.example.SmartziRegistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SmartziRegistration.model.ForgotPasswordForm;
import com.example.SmartziRegistration.model.Mail;
import com.example.SmartziRegistration.model.PasswordForgotDto;
import com.example.SmartziRegistration.model.PasswordResetToken;
import com.example.SmartziRegistration.model.User;
import com.example.SmartziRegistration.repository.PasswordResetTokenRepository;
import com.example.SmartziRegistration.service.EmailService;
import com.example.SmartziRegistration.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
	
	   @Autowired private UserService userService;
	    @Autowired private PasswordResetTokenRepository tokenRepository;
	    @Autowired private EmailService emailService;
	

    @ModelAttribute("forgotPasswordForm")
    public ForgotPasswordForm forgotPasswordForm() {
        return new ForgotPasswordForm();
    }
    
    /*
    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }
    */

    @GetMapping
    public String showForgotPassword(Model model) {
        return "forgot-password";
    }

    @PostMapping
    public String handleForgotPassword(@ModelAttribute("forgotPasswordForm") @Valid ForgotPasswordForm form,
                                       BindingResult result, HttpServletRequest request){

        if (result.hasErrors()){
            return "forgot-password";
        }
        
        User user = userService.findByEmail(form.getEmail());
        if (user == null){
            result.rejectValue("email", null, "We could not find an account for that e-mail address.");
            return "forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "https://memorynotfound.com");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";
    }
    /*
    
    @PostMapping
    public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
                                            BindingResult result,
                                            HttpServletRequest request) {

        if (result.hasErrors()){
            return "forgot-password";
        }

        User user = userService.findByEmail(form.getEmail());
        if (user == null){
            result.rejectValue("email", null, "We could not find an account for that e-mail address.");
            return "forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "https://memorynotfound.com");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";

    }
    
    */

}
