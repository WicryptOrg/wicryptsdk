package com.ugarsoft.wicryptsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import androidx.appcompat.app.AppCompatActivity;

import com.ugarsoft.wicryptsdk_android.Models.LoginModel;
import com.ugarsoft.wicryptsdk_android.Models.SignUpModel;
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

    static int primaryColor;
    public static int colorAccent;
    public static Drawable logo;

    public static String businessId;
    private  UserViewModel user;

    private DefaultCodeGenerator codeGenerator;
    private Activity activity;
    private Context context;

    public Wicrypt(Activity activity,
                   String businessId,
                   UIProperties uiProperties){
        this.activity = activity;
        context = activity.getApplicationContext();
        user = new UserViewModel(activity.getApplicationContext());
        primaryColor = uiProperties.primaryColor;
        colorAccent = uiProperties.colorAccent;
        logo = uiProperties.logo;
        Wicrypt.businessId = businessId;
        codeGenerator = new DefaultCodeGenerator();
    }

    public Wicrypt(Activity activity, String businessId){
        this.activity = activity;
        context = activity.getApplicationContext();
        user = new UserViewModel(activity.getApplicationContext());
        Wicrypt.businessId = businessId;
    }

    public void start(){
        Intent intent;
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
    public boolean userIsLoggedIn(){
        //user = new UserViewModel(activity.getApplicationContext());
        String email = user.getEmail();
        String hashedToken = user.getHashedToken();
        if (email == null || hashedToken == null){
            return false;
        }else{
            return true;
        }
    }

    //check if user owns an account
    public void checkUserExistAndSendTOTP(String email, final Callback callback){
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
    public void loginUser(final LoginModel loginModel,
                                 final Callback callback){
        AuthService authService = new AuthService();
        authService.loginUser(loginModel.getEmail(), loginModel.getOtp(), businessId, new AuthService.onLoginUserCallback() {
            @Override
            public void onSuccess(User user) {
                Wicrypt.this.user.setEmail(loginModel.getEmail());
                Wicrypt.this.user.setHashedToken(user.getHashedToken());
                callback.onSuccess();
            }

            @Override
            public void onFailed(Error error) {
                callback.onFail(error);
            }
        });
    }

    //create new user
    public void createNewUser(final SignUpModel signUpModel,
                                     final Callback callback){

        AuthService authService = new AuthService();

        authService.registerUser(signUpModel.getName(),
                signUpModel.getEmail(),
                signUpModel.getReferrer(),
                signUpModel.getPassword(),
                WifiHelper.getMacAddress(context),
                businessId, new AuthService.onRegisterUserCallback() {
            @Override
            public void onSuccess(User user) {
                Wicrypt.this.user.setEmail(signUpModel.getEmail());
                Wicrypt.this.user.setHashedToken(user.getHashedToken());
                callback.onSuccess();
            }

            @Override
            public void onFailed(Error error) {
                callback.onFail(error);
            }
        });

    }

    //generate TOTP
    //check if user exist else return error
    //if user exist log user in
    public String generateCode(){
        if (!userIsLoggedIn()){
            throw new RuntimeException("User is not logged in");
        }else {
            long counter = new Date().getTime() / 1000 / TOTPActivity.SECONDS_PER_SESSION;
            try {
                String code = codeGenerator.generate(user.getHashedToken(), counter);
                return code;
            }catch (Exception ex){
                return null;
            }
        }
    }

    public void logOut(){
        user.clearData();
    }

    public interface Callback{
        public void onSuccess();
        public void onFail(Error error);
    }

    public static class UIProperties{
        private int primaryColor;
        private int colorAccent;
        private Drawable logo;

        public UIProperties(int primaryColor, int colorAccent, Drawable logo) {
            this.primaryColor = primaryColor;
            this.colorAccent = colorAccent;
            this.logo = logo;
        }
    }

}
