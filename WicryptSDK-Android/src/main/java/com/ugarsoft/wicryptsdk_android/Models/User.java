package com.ugarsoft.wicryptsdk_android.Models;

public class User {
    private String hashedToken;
    private boolean isOTPValid;
    private String otpStatus;

    public String getHashedToken() {
        return hashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }

    public boolean isOTPValid() {
        return isOTPValid;
    }

    public void setOTPValid(boolean OTPValid) {
        isOTPValid = OTPValid;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }
}
