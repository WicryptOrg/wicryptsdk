package com.ugarsoft.wicryptandroidsdkexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ugarsoft.wicryptsdk_android.LoadingButton;
import com.ugarsoft.wicryptsdk_android.Models.LoginModel;
import com.ugarsoft.wicryptsdk_android.Models.Tuple;
import com.ugarsoft.wicryptsdk_android.WTextField;
import com.ugarsoft.wicryptsdk_android.Wicrypt;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    WTextField textField;
    LoadingButton button;
    static String businessId = "";
    Wicrypt wicrypt;
    String email;
    boolean userIsLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textField = findViewById(R.id.text_field);
        button = findViewById(R.id.button);

        Wicrypt.businessId = businessId;
        //Check if user exist
        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        Drawable logo = ContextCompat.getDrawable(this, R.drawable.example_appwidget_preview);
        Wicrypt.UIProperties uiProperties = new Wicrypt.UIProperties(primaryColor, colorAccent, logo);

        wicrypt = new Wicrypt(this, MainActivity.businessId, uiProperties);
        userIsLoggedIn = wicrypt.userIsLoggedIn();
        textView.setText(userIsLoggedIn ? "Logged In" : "logged out");
        button.setText(userIsLoggedIn ? "Generate TOTP" : "Continue");
        textField.setVisibility(userIsLoggedIn ? View.GONE : View.VISIBLE);

    }

    public void Start(View view) {
        if (userIsLoggedIn){
            String totp = wicrypt.generateCode();

            if (totp != null){
                textView.setText(totp);
            }else {
                textView.setText("error");
            }

            return;
        }

        if (email == null){
            //wicrypt.start(this);
            wicrypt.checkUserExistAndSendTOTP(textField.getText(), checkUserAndSendTOTP);
            button.startLoading();
        }else{
            LoginModel  loginModel = new LoginModel(email, textField.getText());
            wicrypt.loginUser(loginModel, loginCallback);
            button.startLoading();
        }
    }

    Wicrypt.Callback checkUserAndSendTOTP = new Wicrypt.Callback() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email = textField.getText();
                    textField.setText("OTP", "", "Enter OTP");
                    button.stopLoading();
                }
            });
        }

        @Override
        public void onFail(final Error error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("User Check Error: " + error.getLocalizedMessage());
                    button.stopLoading();
                }
            });
        }
    };

    Wicrypt.Callback loginCallback = new Wicrypt.Callback() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Logged in");
                    textField.setVisibility(View.GONE);
                    button.stopLoading();
                }
            });
        }

        @Override
        public void onFail(final Error error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Login Error: " + error.getLocalizedMessage());
                    button.stopLoading();
                }
            });
        }
    };

    public void LogOut(View view) {
        wicrypt.logOut();
        userIsLoggedIn = false;
        textView.setText("LogOut");
        button.setText("Continue");
        textField.setVisibility(View.VISIBLE);
    }

    public void ShowUI(View view) {
        wicrypt.start();
    }

    public void ToRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}