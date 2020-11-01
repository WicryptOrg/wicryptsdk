package com.ugarsoft.wicryptandroidsdkexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ugarsoft.wicryptsdk_android.Wicrypt;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String businessId = "WC-dd6b19906c1f4aa2a5353f6e7e406d9f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        Wicrypt.businessId = businessId;
        //Check if user exist
        boolean userExist = Wicrypt.userIsLoggedIn(this);
        textView.setText(userExist ? "true" : "false");

    }

    public void Start(View view) {
        Wicrypt.primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        Wicrypt.colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        Wicrypt.logo = ContextCompat.getDrawable(this, R.drawable.example_appwidget_preview);
        Wicrypt.start(this, "WC-dd6b19906c1f4aa2a5353f6e7e406d9f");
    }
}