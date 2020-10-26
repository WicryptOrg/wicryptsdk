package com.ugarsoft.wicryptsdk_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ugarsoft.wicryptsdk_android.Models.User;
import com.ugarsoft.wicryptsdk_android.services.AuthService;
import com.ugarsoft.wicryptsdk_android.utils.Constants;

public class RegistrationActivity extends AppCompatActivity {
    private LoadingButton registerButton;
    private WTextField nameTextField, pin1TextField, pin2TextField, referralTextField;
    private String email, macAddress;
    private AuthService authService = new AuthService();
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ImageView logoView = findViewById(R.id.logo);
        nameTextField = findViewById(R.id.name);
        pin1TextField = findViewById(R.id.pin1);
        pin2TextField = findViewById(R.id.pin2);
        referralTextField = findViewById(R.id.referrer);
        registerButton = findViewById(R.id.registration_button);
        ImageButton backButton = findViewById(R.id.back_button);

        logoView.setImageDrawable(Wicrypt.logo);
        backButton.setColorFilter(Wicrypt.primaryColor);
        registerButton.setOnClickListener(registerUser);
        ViewCompat.setBackgroundTintList(registerButton, ColorStateList.valueOf(Wicrypt.primaryColor));
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        businessId = intent.getStringExtra(Constants.BUSINESS_ID);

        WifiManager wifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            macAddress = wifiInfo.getMacAddress();
        }
    }

    public void dismiss(View view) {
        finish();
    }

    View.OnClickListener registerUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = nameTextField.getText();
            String pin1 = pin1TextField.getText();
            String pin2 = pin2TextField.getText();
            String referral = referralTextField.getText();
            String macAddress = "";

            if (!pin1.equals(pin2)){
                Toast.makeText(RegistrationActivity.this, "Error: " + "Pin does not match", Toast.LENGTH_LONG).show();
                return;
            }

            if (name.isEmpty() || pin1.isEmpty()){
                Toast.makeText(RegistrationActivity.this, "Error: " + "Enter required fields", Toast.LENGTH_LONG).show();
                return;
            }

            authService.registerUser(name, email, pin1, referral, macAddress, businessId, onRegisterUser);
            registerButton.startLoading();
        }
    };

    AuthService.onRegisterUserCallback onRegisterUser = new AuthService.onRegisterUserCallback() {
        @Override
        public void onSuccess(User user) {
            RegistrationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    registerButton.stopLoading();
                    Intent intent = new Intent(RegistrationActivity.this, TOTPActivity.class);
                    intent.putExtra(Constants.BUSINESS_ID, businessId);
                    RegistrationActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public void onFailed(final Error error) {
            RegistrationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegistrationActivity.this, "Error: " + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    registerButton.stopLoading();
                }
            });
        }
    };
}