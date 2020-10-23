package com.ugarsoft.wicryptsdk_android.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.ugarsoft.wicryptsdk_android.Wicrypt;

public class UserViewModel {
    private String email;
    private String hashedToken;
    private String SHARED_PREFERENCE_ID = "wicrypt.sdk";

    private SharedPreferences sharedPreferences;

    public UserViewModel(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
    }

    public String getEmail() {
        email = sharedPreferences.getString(SHARED_PREFERENCE_ID + ".email", null);
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit()
                .putString(SHARED_PREFERENCE_ID + ".email", email)
                .apply();
    }

    public String getHashedToken() {
        hashedToken = sharedPreferences.getString(SHARED_PREFERENCE_ID + ".hashedToken", "null");
        return hashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
        sharedPreferences.edit()
                .putString(SHARED_PREFERENCE_ID + ".hashedToken", hashedToken)
                .apply();
    }
}
