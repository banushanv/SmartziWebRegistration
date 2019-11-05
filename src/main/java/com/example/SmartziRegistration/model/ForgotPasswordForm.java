package com.example.SmartziRegistration.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.example.SmartziRegistration.constraint.ValidReCaptcha;

public class ForgotPasswordForm {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @ValidReCaptcha
    private String reCaptchaResponse;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReCaptchaResponse() {
        return reCaptchaResponse;
    }

    public void setReCaptchaResponse(String reCaptchaResponse) {
        this.reCaptchaResponse = reCaptchaResponse;
    }
}