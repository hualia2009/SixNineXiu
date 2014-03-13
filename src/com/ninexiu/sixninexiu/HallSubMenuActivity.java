package com.ninexiu.sixninexiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.ninexiu.sixninexiu.R;
import com.umeng.analytics.MobclickAgent;
/**
 * 主播大厅界面 点击弹出主播类型
 * @author mao
 *
 */
public class HallSubMenuActivity extends Activity {
	
	private View topLayout;
	private View hostQueen,hostDiamond,hostStar,hostAtt,hostSee;
	private ImageView iv;
	
	private Handler uiHandler=new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				setResult(200);
				finish();
				break;

			case 200:
				setResult(300);
				finish();
				break;
				
			case 300:
				setResult(400);
				finish();
				break;
				
			case 400:
				Intent intentTrace=new Intent(HallSubMenuActivity.this,TraceHostSeeActivity.class);
				startActivity(intentTrace);
				finish();
				break;
				
			case 500:
				Intent intent=new Intent(HallSubMenuActivity.this,HostHasAttActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_room_sub_menu);
		iv = (ImageView)this.findViewById(R.id.iv_queen);
		iv.setImageResource(R.drawable.host_queen_image);
		topLayout=findViewById(R.id.top_layout);
		hostQueen=findViewById(R.id.host_queen);
		hostDiamond=findViewById(R.id.host_diminod);
		hostStar=findViewById(R.id.host_star);
		hostAtt=findViewById(R.id.host_atte);
		hostSee=findViewById(R.id.host_see);
		topLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setResult(100);
				finish();
				return false;
			}
		});
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.host_queen:
			startAnim(hostQueen, 100);
			break;

		case R.id.host_diminod:
			startAnim(hostDiamond, 200);
			break;
			
		case R.id.host_star:
			startAnim(hostStar, 300);
			break;
			
		case R.id.host_see://我看过的主播列表
			startAnim(hostSee, 400);
			break;
			
		case R.id.host_atte:
			startAnim(hostAtt, 500);
			break;
		}
	}
	
	public static Animation clickAnimation(long durationMillis) {
		AnimationSet set = new AnimationSet(true);
//		set.addAnimation(getAlphaAnimation(1.0f, 0.3f, durationMillis));
		set.addAnimation(getScaleAnimation(durationMillis));
		set.setDuration(durationMillis);
		return set;
	}
	
	public static Animation getAlphaAnimation(float fromAlpha, float toAlpha,
			long durationMillis) {
		AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
		alpha.setDuration(durationMillis);
		alpha.setFillAfter(true);
		return alpha;
	}
	
	public static Animation getScaleAnimation(long durationMillis) {
		ScaleAnimation scale = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(durationMillis);
		return scale;
	}
	
	public static Animation getRestoreAnimation(long durationMillis){
		ScaleAnimation scale = new ScaleAnimation(1.2f, 1.1f, 1.2f, 1.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(durationMillis);
		return scale;
	}
	
	public  void startAnim(final View view,final int what){
		Animation animation=clickAnimation(200);
		view.startAnimation(animation);
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				view.startAnimation(getRestoreAnimation(100));
				uiHandler.sendEmptyMessageDelayed(what, 100);
				
			}
		});
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
