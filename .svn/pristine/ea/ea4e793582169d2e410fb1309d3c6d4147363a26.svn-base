package com.ninexiu.customerview;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ninexiu.sixninexiu.R;

/**
 * 圆角ListView
 */
public class CornerListView extends ListView {

	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
	 private int mMaxYOverscrollDistance;
	 private Context mContext;
    public CornerListView(Context context) {
        this(context, null);
        mContext=context;
        initBounceListView();
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //整个listview的圆角背景
        this.setBackgroundResource(R.drawable.corner_list_bg);
        mContext=context;
        initBounceListView();
    }
   
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION){
                    break;
                } else {
                        if (itemnum == 0){
                                if (itemnum == (getAdapter().getCount()-1)) {
                                    //只有一项
                                    setSelector(R.drawable.corner_list_single_item);
                                } else {
                                    //第一项
                                    setSelector(R.drawable.corner_list_first_item);
                                }
                        } else if (itemnum==(getAdapter().getCount()-1)){
                             //最后一项
                            setSelector(R.drawable.corner_list_last_item);
                        } else {
                            //中间项
                            setSelector(R.drawable.corner_list_item);
                        }
                }
                break;
        case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
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
