package com.ugarsoft.wicryptsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.ugarsoft.wicryptsdk_android.Models.Tuple;
import com.ugarsoft.wicryptsdk_android.Models.User;
import com.ugarsoft.wicryptsdk_android.Models.UserExist;
import com.ugarsoft.wicryptsdk_android.Models.UserViewModel;
import com.ugarsoft.wicryptsdk_android.services.AuthService;
import com.ugarsoft.wicryptsdk_android.utils.Constants;
import com.ugarsoft.wicryptsdk_android.utils.DefaultCodeGenerator;
import com.ugarsoft.wicryptsdk_android.utils.WifiHelper;

import java.util.Date;

public class Wicrypt {
    protected static Class<?> cls;

    public static int primaryColor;
    public static int colorAccent;
    public static Drawable logo;
    public static String businessId;

    public static void start(Activity activity, String businessId){
        Wicrypt.businessId = businessId;
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

    //check if user is logged in
    private static UserViewModel user;
    public static boolean userIsLoggedIn(Activity activity){
        user = new UserViewModel(activity.getApplicationContext());
        String email = user.getEmail();
        String hashedToken = user.getHashedToken();
        if (email == null || hashedToken == null){
            return false;
        }else{
            return true;
        }
    }

    //check if user owns an account
    public static void checkUserExistAndSentOTP(String email, final Callback callback){
        AuthService authService = new AuthService();
        authService.userExist(email, new AuthService.onUserExistCallback() {
            @Override
            public void onSuccess(UserExist userExist) {
                callback.onSuccess();
            }

            @Override
            public void onFailed(Error error) {
                callback.onFail(error);
            }
        });
    }

    //login user
    public static void loginUser(final String email, String otp, String businessId,
                                 final Callback callback){
        AuthService authService = new AuthService();
        authService.loginUser(email, otp, businessId, new AuthService.onLoginUserCallback() {
            @Override
            public void onSuccess(User user) {
                Wicrypt.user.setEmail(email);
                Wicrypt.user.setHashedToken(user.getHashedToken());
                callback.onSuccess();
            }

            @Override
            public void onFailed(Error error) {
                callback.onFail(error);
            }
        });
    }

    //create new user
    public static void createNewUser(Context context, String name, final String email, String referrer,
                                     String password, String businessId,
                                     final Callback callback){

        AuthService authService = new AuthService();

        authService.registerUser(name, email, referrer, password, WifiHelper.getMacAddress(context),
                businessId, new AuthService.onRegisterUserCallback() {
            @Override
            public void onSuccess(User user) {
                Wicrypt.user.setEmail(email);
                Wicrypt.user.setHashedToken(user.getHashedToken());
                callback.onSuccess();
            }

            @Override
            public void onFailed(Error error) {
                callback.onFail(error);
            }
        });

    }

    private static DefaultCodeGenerator codeGenerator;
    //generate TOTP
    //check if user exist else return error
    //if user exist log user in
    public static Tuple<Boolean, String> generateCode(Activity activity){
        if (!userIsLoggedIn(activity)){
            return new Tuple<>(false, "User is not logged in");
        }else {
            if (codeGenerator == null){
                codeGenerator = new DefaultCodeGenerator();
            }
            long counter = new Date().getTime() / 1000 / TOTPActivity.SECONDS_PER_SESSION;
            try {
                String code = codeGenerator.generate(user.getHashedToken(), counter);
                return new Tuple<>(true, code);
            }catch (Exception ex){
                return new Tuple<>(false, ex.getLocalizedMessage());
            }

        }
    }

    public interface Callback{
        public void onSuccess();
        public void onFail(Error error);
    }

}
