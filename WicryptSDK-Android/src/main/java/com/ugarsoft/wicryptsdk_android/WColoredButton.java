package com.ugarsoft.wicryptsdk_android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WColoredButton extends LinearLayout {

    public String placeHolder;

    public WColoredButton(Context context) {
        super(context);
    }

    public WColoredButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WColoredButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initController(Context context){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        layoutInflater.inflate(R.layout.w_text_field, this);
    }
}
