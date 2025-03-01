package com.infteh.comboseekbar;

import java.util.List;

import com.infteh.comboseekbar.ComboSeekBar.Dot;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * seekbar background with text on it.
 * 
 * @author sazonov-adm
 * 
 */
public class CustomDrawable extends Drawable {
	private final ComboSeekBar mySlider;
	private final Drawable myBase;
	private final Paint textUnselected;
	private float mThumbRadius;
	/**
	 * paints.
	 */
	private final Paint unselectLinePaint;
	private List<Dot> mDots;
	private Paint selectLinePaint;
	private Paint circleLinePaint;
	private float mDotRadius;
	private Paint textSelected;
	private int mTextSize;
	private float mTextMargin;
	private int mTextHeight;
	private boolean mIsMultiline;

	public CustomDrawable(Drawable base, ComboSeekBar slider, float thumbRadius, List<Dot> dots, int color, int textSize, boolean isMultiline,int txtColor, int selectedTxtColor) {
		mIsMultiline = isMultiline;
		mySlider = slider;
		myBase = base;
		mDots = dots;
		mTextSize = textSize;
		textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
//		textUnselected.setColor(color);
//		textUnselected.setColor(Color.parseColor("#727171"));
		textUnselected.setColor(txtColor);
		textUnselected.setAlpha(255);

		textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textSelected.setTypeface(Typeface.DEFAULT);
		textSelected.setColor(selectedTxtColor);
		textSelected.setAlpha(255);

		mThumbRadius = thumbRadius;

		unselectLinePaint = new Paint();
//		unselectLinePaint.setColor(color);
//		unselectLinePaint.setColor(Color.parseColor("#D7D7D7"));
		unselectLinePaint.setColor(color);

		unselectLinePaint.setStrokeWidth(toPix(10));

		selectLinePaint = new Paint();
//		selectLinePaint.setColor(color);
//		selectLinePaint.setColor(Color.parseColor("#D7D7D7"));
		selectLinePaint.setColor(color);
		selectLinePaint.setStrokeWidth(toPix(10));

		circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleLinePaint.setColor(color);

		Rect textBounds = new Rect();
		textSelected.setTextSize((int) (mTextSize * 2));
		textSelected.getTextBounds("M", 0, 1, textBounds);

		textUnselected.setTextSize(mTextSize);
		textSelected.setTextSize(mTextSize);

		mTextHeight = textBounds.height();
//		mDotRadius = toPix(5);
//		mTextMargin = toPix(3);
		mDotRadius = toPix(10);
		mTextMargin = toPix(10);
	}

	private float toPix(int size) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mySlider.getContext().getResources().getDisplayMetrics());
	}

	@Override
	protected final void onBoundsChange(Rect bounds) {
		myBase.setBounds(bounds);
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		// Log.d("--- draw:" + (getBounds().right - getBounds().left));
		int height = (this.getIntrinsicHeight() / 2);
		if (mDots.size() == 0) {
			canvas.drawLine(0, height, getBounds().right, height, unselectLinePaint);
			return;
		}
		for (Dot dot : mDots) {
			drawText(canvas, dot, dot.mX, height);
			if (dot.isSelected) {
				canvas.drawLine(mDots.get(0).mX, height, dot.mX, height, selectLinePaint);
				canvas.drawLine(dot.mX, height, mDots.get(mDots.size() - 1).mX, height, unselectLinePaint);
			}
			canvas.drawCircle(dot.mX, height, mDotRadius, circleLinePaint);
		}
	}

	/**
	 * @param canvas
	 *            canvas.
	 * @param dot
	 *            current dot.
	 * @param x
	 *            x cor.
	 * @param y
	 *            y cor.
	 */
	private void drawText(Canvas canvas, Dot dot, float x, float y) {
		final Rect textBounds = new Rect();
		textSelected.getTextBounds(dot.text, 0, dot.text.length(), textBounds);
		float xres;
		if (dot.id == (mDots.size() - 1)) {
			xres = getBounds().width() - textBounds.width();
		} else if (dot.id == 0) {
			xres = 0;
		} else {
			xres = x - (textBounds.width() / 2);
		}

		float yres;
		if (mIsMultiline) {
			if ((dot.id % 2) == 0) {
				yres = y - mTextMargin - mDotRadius;
			} else {
				yres = y + mTextHeight;
			}
		} else {
//			yres = y - (mDotRadius * 2) + mTextMargin;
			yres = y - (toPix(16) * 2) + mTextMargin;
		}

		if (dot.isSelected) {
			canvas.drawText(dot.text, xres, yres, textSelected);
		} else {
			canvas.drawText(dot.text, xres, yres, textUnselected);
		}
	}

	@Override
	public final int getIntrinsicHeight() {
		if (mIsMultiline) {
			return (int) (selectLinePaint.getStrokeWidth() + mDotRadius + (mTextHeight) * 2  + mTextMargin);
		} else {
			return (int) (mThumbRadius + mTextMargin + mTextHeight + mDotRadius/*-toPix(16)*/);
		}
	}

	@Override
	public final int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}