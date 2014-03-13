package com.ninexiu.sixninexiu;

import android.os.Bundle;
import android.widget.ImageView;

import com.ninexiu.sixninexiu.R;
import com.umeng.analytics.MobclickAgent;

public class AboutActivity extends BaseActivity {
	
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		tvTitle.setText("关于六九秀");
		/*View titleView = this.findViewById(R.id.title_layout3);
		titleView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		//Log.e("test","titleView height="+titleView.getMeasuredHeight());
		try {
			int width = this.getResources().getDisplayMetrics().widthPixels;
			int height = this.getResources().getDisplayMetrics().heightPixels;
			
			Rect frame = new Rect();   
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);   
			int statusBarHeight = frame.top;  
			
			int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();   
			int titleBarHeight = contentTop - statusBarHeight;
			//Log.e("test","contentTop="+contentTop+",titleBarHeight="+titleBarHeight+",");		
			height = height - statusBarHeight - titleBarHeight-titleView.getMeasuredHeight();
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			TextView tv = (TextView)findViewById(R.id.about_version);
			imageView = (ImageView) findViewById(R.id.about_nineshow);
			tv.setText("版本："+packageInfo.versionName);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Utils.px2dip(this, 22));
			//Log.e("test","width="+tv.getMeasuredWidth()+",");
			
		
			tv.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int left = (198-tv.getMeasuredWidth())/2;
		    
			AbsoluteLayout.LayoutParams alps = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT,AbsoluteLayout.LayoutParams.WRAP_CONTENT,(256+left)*width/640,495*(height-60)/960);
			tv.setLayoutParams(alps);
		
			Bitmap bm = BitmapFactory.decodeStream(getAssets().open("about.png"));
			imageView.setImageBitmap(bm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	
		//imageView = (ImageView) findViewById(R.id.about_nineshow);
		
		/*
		layoutAbout=(RelativeLayout) findViewById(R.id.layout_about);
		
		final ViewTreeObserver treeObserver=layoutAbout.getViewTreeObserver();
		treeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			public void onGlobalLayout() {
				layoutAbout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				Max_length=layoutAbout.getMeasuredHeight();
				startAutoScrolling();
			}
		});
		tvVersion=(TextView) findViewById(R.id.banben_name);
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			tvVersion.setText(packageInfo.versionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	   public void startAutoScrolling(){
		  timer=new Timer();
		   timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Current_Position=(int) (layoutAbout.getScrollY() + 5.0);
						if(Current_Position >= Max_length){
							Current_Position=-Max_length;
						}
						layoutAbout.scrollTo(0,Current_Position);	
					}
				});
			}
		}, 30, 30);
		 */
		}
		
	
	   @Override
	protected void onDestroy() {
		super.onDestroy();
		/*
		if(timer!=null){
			timer.cancel();
		}
		*/
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
