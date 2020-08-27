package com.fonts.Text;

import android.text.Editable;
import android.text.TextWatcher;

import com.fonts.Text.TextWatcherAdapter.TextWatcherListener;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;

/**
 * To change clear icon, set
 * 
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */

public class ClearableEditText extends AutoCompleteTextView
		implements OnTouchListener, OnFocusChangeListener, TextWatcherListener {

	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public interface Listener {
		void didClearText();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	private Drawable xD;
	private Listener listener;

	public ClearableEditText(Context context) {
		super(context);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		this.l = l;
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener f) {
		this.f = f;
	}

	private OnTouchListener l;
	private OnFocusChangeListener f;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD.getIntrinsicWidth());
			if (tappedX) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					setText("");
					if (listener != null) {
						listener.didClearText();
					}
				}
				return true;
			}
		}
		if (l != null) {
			return l.onTouch(v, event);
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(isNotEmpty(getText()));
		} else {
			setClearIconVisible(false);
		}
		if (f != null) {
			f.onFocusChange(v, hasFocus);
		}
	}

	@Override
	public void onTextChanged(AutoCompleteTextView view, String text) {
		if (isFocused()) {
			setClearIconVisible(isNotEmpty(text));
		}
	}

	private void init() {

		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
			setTypeface(tf);
			this.setPadding(25, 2, 8, 1);
		}

		xD = getCompoundDrawables()[2];
		if (xD == null) {
			xD = getResources().getDrawable(android.R.drawable.presence_offline);
		}
		xD.setBounds(0, 0, xD.getIntrinsicWidth() + 15, xD.getIntrinsicHeight() + 15);
		setClearIconVisible(false);
		super.setOnTouchListener(this);
		super.setOnFocusChangeListener(this);
		addTextChangedListener(new TextWatcherAdapter(this, this));
	}

	protected void setClearIconVisible(boolean visible) {
		boolean wasVisible = (getCompoundDrawables()[2] != null);
		if (visible != wasVisible) {
			Drawable x = visible ? xD : null;
			setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
		}
	}
}

class TextWatcherAdapter implements TextWatcher {

	public interface TextWatcherListener {

		void onTextChanged(AutoCompleteTextView view, String text);

	}

	private final AutoCompleteTextView view;
	private final TextWatcherListener listener;

	public TextWatcherAdapter(AutoCompleteTextView editText, TextWatcherListener listener) {
		this.view = editText;
		this.listener = listener;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		listener.onTextChanged(view, s.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// pass
	}

	@Override
	public void afterTextChanged(Editable s) {
		// pass
	}

}