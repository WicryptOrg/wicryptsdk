package com.ugarsoft.wicryptandroidsdkexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ugarsoft.wicryptsdk_android.LoadingButton;
import com.ugarsoft.wicryptsdk_android.Models.SignUpModel;
import com.ugarsoft.wicryptsdk_android.WTextField;
import com.ugarsoft.wicryptsdk_android.Wicrypt;

public class RegistrationActivity extends AppCompatActivity {

    WTextField emailField, fullNameField, pinField, referralCodeFiled;
    TextView messageView;
    Wicrypt wicrypt;
    LoadingButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailField = findViewById(R.id.email_field);
        fullNameField = findViewById(R.id.full_name_field);
        pinField = findViewById(R.id.pin_field);
        referralCodeFiled = findViewById(R.id.referral_field);
        messageView = findViewById(R.id.message);
        messageView.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.button);

        wicrypt = new Wicrypt(this, MainActivity.businessId);
    }

    public void Register(View view) {
        SignUpModel signUpModel = new SignUpModel(
                fullNameField.getText(),
                emailField.getText(),
                referralCodeFiled.getText(),
                pinField.getText(),
                MainActivity.businessId);
        wicrypt.createNewUser(signUpModel, callback);
        messageView.setVisibility(View.INVISIBLE);
        button.startLoading();
    }

    Wicrypt.Callback callback = new Wicrypt.Callback() {
        @Override
        public void onSuccess() {
            button.disable();
            messageView.setText(R.string.registration_complete);
            messageView.setVisibility(View.VISIBLE);
            button.stopLoading();
        }

        @Override
        public void onFail(final Error error) {
            RegistrationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.enable();
                    messageView.setText(error.getLocalizedMessage());
                    messageView.setVisibility(View.VISIBLE);
                    button.stopLoading();
                }
            });
        }
    };
}