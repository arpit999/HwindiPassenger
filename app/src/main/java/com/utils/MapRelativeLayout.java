package com.utils;

import java.util.Calendar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.hwindiapp.passenger.Mainprofile_Activity;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

public class MapRelativeLayout extends RelativeLayout {
	private GestureDetector gestureDetector;
	public ScaleGestureDetector scl;

	final int MAX_CLICK_DURATION = 100;
	long startClickTime = 0;

	private static final int MAX_CLICK_DISTANCE = 15;

	private float pressedX;
	private float pressedY;

	Mainprofile_Activity mainActivity;

	boolean isAnimFinished = true;

	public void setMainProfileHandler(Mainprofile_Activity mainActivity) {
		this.mainActivity = mainActivity;

	}

	public MapRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(context, new GestureListener());
		scl = new ScaleGestureDetector(context, new Scale());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		scl.onTouchEvent(ev);
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;

			float distance = distance(pressedX, pressedY, ev.getX(), ev.getY());

			if (clickDuration > MAX_CLICK_DURATION && distance > MAX_CLICK_DISTANCE) {
				// click event has occurred
				mainActivity.handleMapDrag();
			}

			break;

		case MotionEvent.ACTION_UP:

			break;

		case MotionEvent.ACTION_DOWN:

			startClickTime = Calendar.getInstance().getTimeInMillis();

			// Log.d("startClickTime", ":"+startClickTime);
			pressedX = ev.getX();
			pressedY = ev.getY();
			break;

		}

		return super.dispatchTouchEvent(ev);
	}

	private float distance(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
		return pxToDp(distanceInPx);
	}

	private float pxToDp(float px) {
		return px / getResources().getDisplayMetrics().density;
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener
	/* implements OnScaleGestureListener */ {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// mainActivity.CustomMapTouch("Double Tap", 0);

			GoogleMap map = mainActivity.getMap();

			if (map != null) {

				LatLng target = map.getCameraPosition().target;

				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target, map.getCameraPosition().zoom + 1);

				map.animateCamera(update, 400, new GoogleMap.CancelableCallback() {
					@Override
					public void onFinish() {

					}

					@Override
					public void onCancel() {

					}
				});
			}
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// do whatever you want here

			// GoogleMap map = mainActivity.getMap();
			// LatLng target = map.getCameraPosition().target;
			// Point screenPoint = map.getProjection().toScreenLocation(target);
			// Point newPoint = new Point(screenPoint.x + (int)distanceX,
			// screenPoint.y + (int)distanceY);
			// LatLng mapNewTarget =
			// map.getProjection().fromScreenLocation(newPoint);
			// CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
			// mapNewTarget,map.getCameraPosition().zoom);
			//
			// map.animateCamera(update, 400, new GoogleMap.CancelableCallback()
			// {
			// @Override
			// public void onFinish() {
			// }
			// @Override
			// public void onCancel() {
			// }
			// });

			return false;
		}

		/*
		 * @Override public boolean onScale(android.view.ScaleGestureDetector
		 * detector) { // TODO Auto-generated method stub Log.d("Scale on",
		 * "Yes:"); return false; }
		 * 
		 * @Override public boolean
		 * onScaleBegin(android.view.ScaleGestureDetector detector) { // TODO
		 * Auto-generated method stub
		 * 
		 * Log.d("Scale begin", "Yes:");
		 * 
		 * return false; }
		 * 
		 * @Override public void onScaleEnd(android.view.ScaleGestureDetector
		 * detector) { // TODO Auto-generated method stub
		 * 
		 * Log.d("Scale end", "Yes:");
		 * 
		 * }
		 */
	}

	public class Scale implements ScaleGestureDetector.OnScaleGestureListener {
		float startScale;
		float endScale;

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			// Log.d("Scale on", "Yes:"+detector.getScaleFactor());

			// if (startScale > detector.getScaleFactor()) {
			// //zoom out
			//
			// mainActivity.CustomMapTouch("Zoom
			// Out",detector.getScaleFactor()+1);
			// }else if(detector.getScaleFactor()>startScale){
			// // zoom in
			//
			// mainActivity.CustomMapTouch("Zoom In",detector.getScaleFactor());
			// }

			GoogleMap map = mainActivity.getMap();

			if (map != null) {

				double zoom = map.getCameraPosition().zoom;

				LatLng target = map.getCameraPosition().target;

//				zoom = zoom + Math.log(detector.getScaleFactor()) / Math.log(2d);

				double scale = 0;
				if (detector.getScaleFactor() > 1) {
					scale = detector.getScaleFactor() + 0.05;
				} else {
					scale = detector.getScaleFactor() - 0.05;
				}

				zoom = zoom + Math.log(scale) / Math.log(2d);

//				 Log.d("zoom", "zoom:" + zoom);

				if (isAnimFinished) {

					CameraUpdate update = CameraUpdateFactory.newLatLngZoom(target, (float) zoom);
					isAnimFinished = false;
					map.moveCamera(update);

					isAnimFinished = true;


				}
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			startScale = detector.getScaleFactor();
			// Log.d("Scale begin", "Yes:"+startScale);
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			endScale = detector.getScaleFactor();
			
		}

	}

}
