package com.hwindiapp.passenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class RippleStyleButton extends Button {

	private TouchEffectAnimator touchEffectAnimator;
	private static Typeface mTypeface;

	public RippleStyleButton(Context context) {
		super(context);
		init();
	}

	public RippleStyleButton(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);

	}

	public RippleStyleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		// you should set a background to view for effect to be visible. in this
		// sample, this
		// linear layout contains a transparent background which is set inside
		// the XML

		// giving the view to animate on
		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
		}
		this.setTypeface(mTypeface);

		touchEffectAnimator = new TouchEffectAnimator(this);

		// enabling ripple effect. it only performs ease effect without enabling
		// ripple effect
		touchEffectAnimator.setHasRippleEffect(true);

		// setting the effect color
		touchEffectAnimator.setEffectColor(Color.parseColor("#454545"));

		// setting the duration
		touchEffectAnimator.setAnimDuration(1000);

		// setting radius to clip the effect. use it if you have a rounded
		// background
		touchEffectAnimator.setClipRadius(2);

		// the view must contain an onClickListener to receive UP touch events.
		// touchEffectAnimator
		// doesn't return any value in onTouchEvent for flexibility. so it is
		// developers
		// responsibility to add a listener
		// setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// }
		// });
	}

	public void setColor(int color_str) {

//		touchEffectAnimator.setEffectColor(Color.parseColor(color_str));
		touchEffectAnimator.setEffectColor(color_str);
		// this.color_str=color_str;
	}

	public void setClipRadius(int radius) {

		touchEffectAnimator.setClipRadius(radius);
		// this.color_str=color_str;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// send the touch event to animator
		touchEffectAnimator.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		// let animator show the animation by applying changes to view's canvas
		touchEffectAnimator.onDraw(canvas);
		super.onDraw(canvas);
	}
}
