package com.ninexiu.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GalleryView extends Gallery {

	public GalleryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GalleryView(Context context) {
		super(context);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		velocityX = velocityX / 4;
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		float distanceX2 = distanceX * 1.5f;
		float distanceY2 = distanceY * 1.5f;
		return super.onScroll(e1, e2, distanceX2, distanceY2);
	}

}
