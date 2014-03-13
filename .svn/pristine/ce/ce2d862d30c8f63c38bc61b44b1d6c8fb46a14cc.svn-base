package com.ninexiu.customerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class PullListView extends ListView{
	
	private Context mContext;
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
	 private int mMaxYOverscrollDistance;
	
	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		 initBounceListView();
	}
	
	public PullListView(Context context){
		super(context);
	}
	
	  private void initBounceListView() {
	        //get the density of the screen and do some maths with it on the max overscroll distance
	        //variable so that you get similar behaviors no matter what the screen size
	        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
	        final float density = metrics.density;
	        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	    }
	    
	    @SuppressLint("NewApi")
		@Override
	    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
	    		int scrollY, int scrollRangeX, int scrollRangeY,
	    		int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
	    	// TODO Auto-generated method stub
	    	return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
	    			scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	    }
} 



