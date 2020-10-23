package com.ugarsoft.wicryptsdk_android.Models;

public class UserExist {
    private boolean didOtpEmailSend;
    private  boolean doesUserExist;

    public boolean getDidOtpEmailSend() {
        return didOtpEmailSend;
    }

    public void setDidOtpEmailSend(boolean didOtpEmailSend) {
        this.didOtpEmailSend = didOtpEmailSend;
    }

    public boolean getDoesUserExist() {
        return doesUserExist;
    }

    public void setDoesUserExist(boolean doesUserExist) {
        this.doesUserExist = doesUserExist;
    }
}
