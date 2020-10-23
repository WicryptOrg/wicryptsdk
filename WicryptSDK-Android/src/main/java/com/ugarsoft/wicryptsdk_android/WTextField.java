package com.ugarsoft.wicryptsdk_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class WTextField extends LinearLayout {

    private String placeHolder;
    private String title;
    private EditText editTextView;
    private TextView titleView;

    public WTextField(Context context) {
        super(context);
        initController(context, null);
    }

    public WTextField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initController(context, attrs);
    }

    public WTextField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initController(context, attrs);
    }

    private void initController(Context context, AttributeSet attr){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        layoutInflater.inflate(R.layout.w_text_field, this);

        titleView = findViewById(R.id.title);
        editTextView = findViewById(R.id.edit_text);

        loadAttribute(attr);

        titleView.setText(title);
        editTextView.setHint(placeHolder);
    }

    private void loadAttribute(AttributeSet attr){
        TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.WTextField);

        try {
           title = typedArray.getString(R.styleable.WTextField_title);
           placeHolder = typedArray.getString(R.styleable.WTextField_placeHolder);
        }finally {
            typedArray.recycle();
        }
    }

    public String getText(){
        return editTextView.getText().toString();
    }

    public void setText(String title, String text, String placeHolder){
        this.titleView.setText(title);
        this.editTextView.setText(text);
        this.editTextView.setHint(placeHolder);
    }
}
