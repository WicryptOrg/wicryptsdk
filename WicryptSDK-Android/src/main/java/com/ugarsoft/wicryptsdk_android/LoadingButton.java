package com.ugarsoft.wicryptsdk_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class LoadingButton extends LinearLayout {

    String title;
    int mainColor, colorAccent;
    ProgressBar progressBar;
    TextView textView;

    public LoadingButton(Context context) {
        super(context);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initController(context, attrs);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initController(context, attrs);
    }

    private void initController(Context context, AttributeSet attributeSet){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        layoutInflater.inflate(R.layout.loading_button, this);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.title);

        loadAttributes(context, attributeSet);

        textView.setText(title);
        stopLoading();
    }

    private void loadAttributes(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LoadingButton);
        try {
            title = typedArray.getString(R.styleable.LoadingButton_titleX);
            //mainColor = typedArray.getInt(R.styleable.LoadingButton_color, Color.BLUE);
            colorAccent = typedArray.getInt(R.styleable.LoadingButton_colorAccentX, Color.WHITE);
        }catch (Exception e){
            typedArray.recycle();
        }
    }

    public void startLoading(){
        progressBar.setVisibility(VISIBLE);
        textView.setVisibility(GONE);
        this.setEnabled(false);
    }

    public void stopLoading(){
        progressBar.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
        setEnabled(true);
    }

    public void setText(int text){

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}
