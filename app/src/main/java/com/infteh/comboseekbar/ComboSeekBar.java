package com.infteh.comboseekbar;

import java.util.ArrayList;
import java.util.List;

import com.hwindiapp.passenger.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SeekBar;

public class ComboSeekBar extends SeekBar {
	private CustomThumbDrawable mThumb;
	public List<Dot> mDots = new ArrayList<Dot>();
	public OnItemClickListener mItemClickListener;
	private Dot prevSelected = null;
	private boolean isSelected = false;
	private int mColor;
	private int mTextSize;
	private boolean mIsMultiline;

	/**
	 * @param context
	 *            context.
	 */
	public ComboSeekBar(Context context) {
		super(context);
	}

	/**
	 * @param context
	 *            context.
	 * @param attrs
	 *            attrs.
	 */
	public ComboSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ComboSeekBar);

		mColor = a.getColor(R.styleable.ComboSeekBar_myColor, Color.WHITE);
		int mThumbColor = a.getColor(R.styleable.ComboSeekBar_thumbColor, Color.WHITE);
		int txtColor = a.getColor(R.styleable.ComboSeekBar_txtColor, Color.WHITE);
		int selectedTxtColor = a.getColor(R.styleable.ComboSeekBar_selectedTxtColor, Color.WHITE);
		mTextSize = a.getDimensionPixelSize(R.styleable.ComboSeekBar_textSize, 5);
		mIsMultiline = a.getBoolean(R.styleable.ComboSeekBar_multiline, false);
		// do something with str

		a.recycle();
		mThumb = new CustomThumbDrawable(context, mThumbColor);
//		mThumb.getBounds().bottom += (int) toPix(55);
		setThumb(mThumb);
		setProgressDrawable(new CustomDrawable(this.getProgressDrawable(), this,
				20/* mThumb.getRadius() */, mDots, mColor, mTextSize, mIsMultiline,txtColor,selectedTxtColor));

		setPadding(0, (int)toPix(10), 0, 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isSelected = false;
		return super.onTouchEvent(event);
	}

//	/**
//	 * @param color
//	 *            color.
//	 */
//	public void setColor(int color) {
//		mColor = color;
//		mThumb.setColor(color);
//		setProgressDrawable(new CustomDrawable((CustomDrawable) this.getProgressDrawable(), this,
//				20/* mThumb.getRadius() */, mDots, color, mTextSize, mIsMultiline));
//	}

	public synchronized void setSelection(int position) {
		if ((position < 0) || (position >= mDots.size())) {
			throw new IllegalArgumentException("Position is out of bounds:" + position);
		}
		for (Dot dot : mDots) {
			if (dot.id == position) {
				dot.isSelected = true;
				handleClick(dot);
			} else {
				dot.isSelected = false;
			}
		}

		isSelected = true;
		invalidate();
	}

	public void setAdapter(List<String> dots) {
		mDots.clear();
		int index = 0;
		for (String dotName : dots) {
			Dot dot = new Dot();
			dot.text = dotName;
			dot.id = index++;
			mDots.add(dot);
		}
		initDotsCoordinates();
	}

//	@Override
//	public void setThumb(Drawable thumb) {
//		if (thumb instanceof CustomThumbDrawable) {
//			mThumb = (CustomThumbDrawable) thumb;
//		}
//		super.setThumb(thumb);
//	}

	float toPix(int size) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, this.getResources().getDisplayMetrics());
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		if ((mThumb != null) && (mDots.size() > 1)) {
			if (isSelected) {
				for (Dot dot : mDots) {
					if (dot.isSelected) {
						Rect bounds = mThumb.copyBounds();
						bounds.right = dot.mX;
						bounds.left = dot.mX;
						mThumb.setBounds(bounds);
						break;
					}
				}
			} else {
				int intervalWidth = mDots.get(1).mX - mDots.get(0).mX;
				Rect bounds = mThumb.copyBounds();
				// find nearest dot
				if ((mDots.get(mDots.size() - 1).mX - bounds.centerX()) < 0) {
					bounds.right = mDots.get(mDots.size() - 1).mX;
					bounds.left = mDots.get(mDots.size() - 1).mX;
					mThumb.setBounds(bounds);

					for (Dot dot : mDots) {
						dot.isSelected = false;
					}
					mDots.get(mDots.size() - 1).isSelected = true;
					handleClick(mDots.get(mDots.size() - 1));
				} else {
					for (int i = 0; i < mDots.size(); i++) {
						if (Math.abs(mDots.get(i).mX - bounds.centerX()) <= (intervalWidth / 2)) {
							bounds.right = mDots.get(i).mX;
							bounds.left = mDots.get(i).mX;
							mThumb.setBounds(bounds);
							mDots.get(i).isSelected = true;
							handleClick(mDots.get(i));
						} else {
							mDots.get(i).isSelected = false;
						}
					}
				}
			}
		}
		super.onDraw(canvas);
	}

	private void handleClick(Dot selected) {
		if ((prevSelected == null) || (prevSelected.equals(selected) == false)) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(null, this, selected.id, selected.id);
			}
			prevSelected = selected;
		}
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		CustomDrawable d = (CustomDrawable) getProgressDrawable();

		int thumbHeight = mThumb == null ? 0 : mThumb.getIntrinsicHeight();
		int dw = 0;
		int dh = 0;
		if (d != null) {
			dw = d.getIntrinsicWidth();
			dh = Math.max(thumbHeight, d.getIntrinsicHeight());
		}

		dw += getPaddingLeft() + getPaddingRight();
		dh += getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(resolveSize(dw, widthMeasureSpec), resolveSize(dh, heightMeasureSpec));
	}

	/**
	 * dot coordinates.
	 */
	private void initDotsCoordinates() {
		float intervalWidth = (getWidth() - (mThumb.getRadius() * 2)) / (mDots.size() - 1);
		for (Dot dot : mDots) {
			dot.mX = (int) (mThumb.getRadius() + intervalWidth * (dot.id));
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initDotsCoordinates();
	}

	/**
	 * Sets a listener to receive events when a list item is clicked.
	 * 
	 * @param clickListener
	 *            Listener to register
	 * 
	 * @see ListView#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)
	 */
	public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
		mItemClickListener = clickListener;
	}

	public static class Dot {
		public int id;
		public int mX;
		public String text;
		public boolean isSelected = false;

		@Override
		public boolean equals(Object o) {
			return ((Dot) o).id == id;
		}
	}
}
