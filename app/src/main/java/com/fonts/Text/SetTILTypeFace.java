package com.fonts.Text;


import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.TextView;

public class SetTILTypeFace {

	public static void setTilTypeFace(Context mContext, TextInputLayout til) {
		final Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");
		til.setTypeface(tf);
		
		til.getEditText().setTypeface(tf);
		til.setErrorEnabled(false);
		try {
			final Field errorField = til.getClass().getDeclaredField("mErrorView");
			errorField.setAccessible(true);
			((TextView) errorField.get(til)).setTypeface(tf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
	}

}
