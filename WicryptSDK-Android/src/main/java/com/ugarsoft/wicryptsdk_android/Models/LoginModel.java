package com.ugarsoft.wicryptsdk_android.Models;

public class LoginModel {
    private String email;
    private String otp;

    public LoginModel(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
}
