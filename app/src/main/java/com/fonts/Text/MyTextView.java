package com.fonts.Text;

import com.hwindiapp.passenger.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class MyTextView extends AppCompatTextView {

/*
 * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
 */
private static Typeface mTypeface;

public MyTextView(final Context context) {
    this(context, null);
}

public MyTextView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
}

public MyTextView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

     if (mTypeface == null) {
         mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
     }
     setTypeface(mTypeface);
     
//     this.setTextSize(22);
}

}
