package com.ugarsoft.wicryptsdk_android.Models;

public class SignUpModel {
    private String name;
    private String email;
    private String referrer;
    private String password;
    private String businessId;

    public SignUpModel(String name, String email, String referrer, String password, String businessId) {
        this.name = name;
        this.email = email;
        this.referrer = referrer;
        this.password = password;
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getPassword() {
        return password;
    }

    public String getBusinessId() {
        return businessId;
    }
}
