package com.ugarsoft.wicryptsdk_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ugarsoft.wicryptsdk_android.Models.User;
import com.ugarsoft.wicryptsdk_android.Models.UserExist;
import com.ugarsoft.wicryptsdk_android.Models.UserViewModel;
import com.ugarsoft.wicryptsdk_android.services.AuthService;
import com.ugarsoft.wicryptsdk_android.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private AuthService authService = new AuthService();
    private WTextField textField;
    private LoadingButton loginButton;
    private boolean userExist = false;
    private String email = "";
    private UserViewModel userViewModel;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getActionBar() != null){
            getActionBar().hide();
        }

        businessId = getIntent().getStringExtra(Constants.BUSINESS_ID);

        ImageView logoView = findViewById(R.id.logo);
        loginButton = findViewById(R.id.login_button);
        loginButton.setText(R.string.next);
        loginButton.setOnClickListener(loginButtonClicked);
        ViewCompat.setBackgroundTintList(loginButton, ColorStateList.valueOf(Wicrypt.primaryColor));
        textField = findViewById(R.id.text_field);
        ImageButton backButton = findViewById(R.id.back_button);

        logoView.setImageDrawable(Wicrypt.logo);
        backButton.setColorFilter(Wicrypt.primaryColor);
        userViewModel = new UserViewModel(this);
        email = userViewModel.getEmail();
        if (email != null){
            textField.setText(email);
        }
    }

    public void dismiss(View view) {
        finish();
    }

    View.OnClickListener loginButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!userExist){
                userViewModel.setEmail(textField.getText());
                authService.userExist(textField.getText(), onUserExistCallback);
            }else if (userExist){
                authService.loginUser(email, textField.getText(), businessId, onLoginUserCallback);
            }
            loginButton.startLoading();
        }
    };

    private void navigateToRegistration(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra(Constants.BUSINESS_ID, businessId);
        startActivity(intent);
    }

    AuthService.onUserExistCallback onUserExistCallback = new AuthService.onUserExistCallback() {
        @Override
        public void onSuccess(final UserExist userExist) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userExist.getDoesUserExist()){
                        loginButton.setText(R.string.login);
                        LoginActivity.this.userExist = true;
                        LoginActivity.this.email = textField.getText();
                        textField.setText("Enter OTP", "", "OTP");
                    }else{
                        navigateToRegistration();
                    }
                    loginButton.stopLoading();
                }
            });
        }

        @Override
        public void onFailed(final Error error) {
            //show failure message
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.this.userExist = false;
                    Toast.makeText(LoginActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    loginButton.stopLoading();
                }
            });
        }
    };


    AuthService.onLoginUserCallback onLoginUserCallback = new AuthService.onLoginUserCallback() {
        @Override
        public void onSuccess(final User user) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userViewModel.setEmail(email);
                    userViewModel.setHashedToken(user.getHashedToken());
                    Intent intent = new Intent(LoginActivity.this, TOTPActivity.class);
                    intent.putExtra(Constants.BUSINESS_ID, businessId);
                    LoginActivity.this.startActivity(intent);
                    loginButton.stopLoading();
                }
            });
        }

        @Override
        public void onFailed(final Error error) {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    loginButton.stopLoading();
                }
            });
        }
    };
}