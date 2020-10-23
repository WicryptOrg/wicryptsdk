package com.ugarsoft.wicryptsdk_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ugarsoft.wicryptsdk_android.services.Constants;

import java.net.URI;

public class TCView extends LinearLayout {
    public TCView(Context context) {
        super(context);
        initController(context);
    }

    public TCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initController(context);
    }

    public TCView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initController(context);
    }


    private void initController(Context context){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        layoutInflater.inflate(R.layout.t_c_view, this);

        TextView tAndC = findViewById(R.id.t_and_c);
        tAndC.setText(getTAndC(context));
        tAndC.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString getTAndC(final Context context){
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.terms_and_condition));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.TERMS_AND_CONDITION_URL));
                context.startActivity(intent);
            }
        };
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Wicrypt.colorAccent);
        spannableString.setSpan(clickableSpan, 0, 17, spannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, 0, 17, spannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
