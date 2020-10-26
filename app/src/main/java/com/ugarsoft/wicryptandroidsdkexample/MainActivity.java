package com.ugarsoft.wicryptandroidsdkexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.ugarsoft.wicryptsdk_android.Wicrypt;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Start(View view) {
        Wicrypt.primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);
        Wicrypt.colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        Wicrypt.logo = ContextCompat.getDrawable(this, R.drawable.example_appwidget_preview);
        Wicrypt.start(this, "5Y4op5eLnBlWDj9nybpgpP6aaD3zktOaLyEG6FvhT8Jg4nMVJV+uPtmo4JBUjLhm");
    }
}