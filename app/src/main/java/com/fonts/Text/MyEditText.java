package com.fonts.Text;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hwindiapp.passenger.R;

public class MyEditText extends EditText{

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
            setTypeface(tf);
            this.setPadding(25, 2, 2, 1);
        }

//        setTextColor(getResources().getColor(R.color.editbox_text_color));
    }

 }
