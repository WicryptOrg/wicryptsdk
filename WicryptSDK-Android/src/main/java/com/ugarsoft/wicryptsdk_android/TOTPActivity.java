package com.ugarsoft.wicryptsdk_android;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.ugarsoft.wicryptsdk_android.Models.UserViewModel;
import com.ugarsoft.wicryptsdk_android.utils.DefaultCodeGenerator;
import com.ugarsoft.wicryptsdk_android.utils.WifiHelper;

import java.util.ArrayList;
import java.util.Date;

import douglasspgyn.com.github.circularcountdown.CircularCountdown;
import douglasspgyn.com.github.circularcountdown.listener.CircularListener;

public class TOTPActivity extends AppCompatActivity {

    UserViewModel user;
    private TextView emailView, totpView, connectionStatusView, connectionSSIDView;
    private Button loginButton;
    private ClipboardManager clipboard;
    private CircularCountdown circularCountdown;
    private DefaultCodeGenerator defaultCodeGenerator;
    public static final int SECONDS_PER_SESSION = 30;
    private WifiManagerReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_o_t_p);

        user = new UserViewModel(this);
        defaultCodeGenerator = new DefaultCodeGenerator();

        emailView = findViewById(R.id.emailView);
        totpView = findViewById(R.id.otpView);
        connectionStatusView = findViewById(R.id.connection_state);
        circularCountdown = findViewById(R.id.countdown);
        connectionSSIDView = findViewById(R.id.connection_ssid);
        loginButton = findViewById(R.id.login_button);
        ImageButton backButton = findViewById(R.id.back_button);

        backButton.setColorFilter(Wicrypt.primaryColor);
        connectionSSIDView.setTextColor(Wicrypt.colorAccent);
        emailView.setTextColor(Wicrypt.primaryColor);
        ViewCompat.setBackgroundTintList(loginButton, ColorStateList.valueOf(Wicrypt.primaryColor));

        emailView.setText(user.getEmail());
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        circularCountdown.create(0 , 30, CircularCountdown.TYPE_SECOND)
                .listener(circularCountdownListener)
                .start();
        totpView.setText(generateTOTP());
 //       circularCountdown.setProgressBackgroundColor(Wicrypt.colorAccent);
//        circularCountdown.setProgressForegroundColor(Wicrypt.primaryColor);
        displayConnectionInfo();
    }

    @Override
    protected void onStart() {
        if (receiver == null) {
            registerReceivers();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onStop();
    }

    private String generateTOTP(){
        long counter = new Date().getTime() / 1000 / SECONDS_PER_SESSION;
        try {
            return defaultCodeGenerator.generate(user.getHashedToken(), counter);
        }catch (Exception e){
            return "Error";
        }
    }

    public void copyEmail(View view) {
        copyText("Your Email", "");
        Toast.makeText(getApplicationContext(), "email copied", Toast.LENGTH_LONG).show();
    }

    public void copyCode(View view) {
        copyText("TOTP code", "");
        Toast.makeText(getApplicationContext(), "TOTP copied", Toast.LENGTH_LONG).show();
    }

    private void copyText(String label, String text){
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
        }
    }

    public void goToLoginPage(View view) {
        String url = String.format("%s?", "http://192.168.4.4:5000/spikesplash_auth/?");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void seeDevices(View view) {
    }

    private void displayConnectionInfo(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID() != null && WifiHelper.isWicryptProDevice(wifiInfo.getSSID())){
            connectionStatusView.setText("Connected");
            connectionSSIDView.setText(wifiInfo.getSSID());
            connectionSSIDView.setVisibility(View.VISIBLE);
            loginButton.setAlpha(1);
            loginButton.setEnabled(true);
        }else{
            connectionStatusView.setText("Disconnected");
            connectionSSIDView.setVisibility(View.INVISIBLE);
            loginButton.setAlpha(0.5f);
            loginButton.setEnabled(false);
        }
    }

    CircularListener circularCountdownListener = new CircularListener() {
        @Override
        public void onTick(int i) {
            //circularCountdown.setProgress(i);
        }

        @Override
        public void onFinish(boolean b, int i) {
            totpView.setText(generateTOTP());
        }
    };

    private void registerReceivers() {
        receiver = new WifiManagerReceiver();
        registerReceiver(receiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        registerReceiver(receiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    public void dismiss(View view) {
        //Wicrypt.dismiss(this);
        finish();
        circularCountdown.stop();
        circularCountdown.disableLoop();
    }

    public void LogOut(View view) {
        user.clearData();
        finish();
    }

    class WifiManagerReceiver extends BroadcastReceiver {

        @Override
        @UiThread
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();

            if (intentAction != null) {
                switch (intentAction) {
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        displayConnectionInfo();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}