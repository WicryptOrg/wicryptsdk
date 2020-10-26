package com.ugarsoft.wicryptsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.ugarsoft.wicryptsdk_android.Models.UserViewModel;
import com.ugarsoft.wicryptsdk_android.utils.Constants;

public class Wicrypt {
    protected static Class<?> cls;

    public static int primaryColor;
    public static int colorAccent;
    public static Drawable logo;

    public static void start(Activity activity, String businessId){
        Intent intent;
        UserViewModel user = new UserViewModel(activity.getApplicationContext());
        String email = user.getEmail();
        String hashedToken = user.getHashedToken();
        if (email == null || hashedToken == null){
            intent = new Intent(activity, LoginActivity.class);
        }else{
            intent = new Intent(activity, TOTPActivity.class);
        }
        intent.putExtra(Constants.BUSINESS_ID, businessId);
        activity.startActivity(intent);
    }

    public static void dismiss(Activity activity){
        Intent intent = new Intent(activity.getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //activity.startActivity(intent);
    }
}
