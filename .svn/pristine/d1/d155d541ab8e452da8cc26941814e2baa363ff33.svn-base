package com.ninexiu.utils;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.R;
import com.yishi.sixshot.IFlyMediaCallback;
import com.yishi.sixshot.player.FlyRtmpPlayer;

public class PlayerViewController implements IFlyMediaCallback {
	
	private ChatRoomActivity mainActivity = null;
	public static final int VIDEO_LAYOUT_ZOOM = 3;
	public static final int VIDEO_LAYOUT_SCALE = 1;
	private FlyRtmpPlayer rtmpPlayer = null;
//	private AnimationDrawable animationDrawable;
	private View ivLoad;
	private boolean isPlay;
	
	public PlayerViewController(ChatRoomActivity activity, AnimationDrawable drawable, ImageView ivVideoLoad)
	{
		mainActivity = activity;
//		this.animationDrawable=drawable;
		this.ivLoad=ivVideoLoad;
	}
	public PlayerViewController(ChatRoomActivity activity, View ivVideoLoad)
	{
		mainActivity = activity;
//		this.animationDrawable=drawable;
		this.ivLoad=ivVideoLoad;
	}
	public boolean initPlayerView()
	{
		rtmpPlayer = new FlyRtmpPlayer(mainActivity);
		rtmpPlayer.setStatusCallback(this);
		
		RelativeLayout viewLayout = (RelativeLayout) mainActivity.findViewById(R.id.surfaceview);
		if(viewLayout == null)
			return false;
		int screenWidth=mainActivity.getWindowManager().getDefaultDisplay().getWidth();
		float radio=3f/4f;
		int videoHeight=(int)(screenWidth*radio);
//		int w = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, mainActivity.getResources().getDisplayMetrics());
//		int h = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, mainActivity.getResources().getDisplayMetrics());
//		Log.e("VideoHeight",videoHeight+"");
		SurfaceView sView = rtmpPlayer.getVideoView(new Point(screenWidth,videoHeight));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
		sView.setLayoutParams(lp);
		viewLayout.addView(sView);
		if(!rtmpPlayer.initPlayer())
			return false;
		
		//如果需要再修改视频View的大小，如横竖屏后，调用下面方法
//		rtmpPlayer.setVideoViewSize(screenWidth,videoHeight);

		return true;
	}
	
	public void setVideoSize(int layout){
		DisplayMetrics disp = mainActivity.getResources().getDisplayMetrics();
	    int windowWidth = disp.widthPixels, windowHeight = disp.heightPixels;
	    if(layout==VIDEO_LAYOUT_ZOOM){
			float radio=3f/4f;
			int videoHeight=(int)(windowWidth*radio);
	    	rtmpPlayer.setVideoViewSize(windowWidth,videoHeight);
	    }else{
	    	float videoRatio=4f/3f;
			int width=(int) (windowHeight*videoRatio);
			rtmpPlayer.setVideoViewSize(width,windowHeight);
	    }
		
	}
	
	/*
	 * 开始播放
	 */
	public void startPlay(String roomTag,String roomId){
		//room_ext1=0:  rtmp://videodownls.9xiu.com:1935/9xiu/123
		//room_ext1=1:  rtmp://videodownws.9xiu.com:1935/9xiu/123
		String rtmpUrl=null;
		if(roomTag.equals("0")){
			rtmpUrl= "rtmp://videodownls.9xiu.com:1935/9xiu/"+roomId;
		}else{
			rtmpUrl="rtmp://videodownws.9xiu.com:1935/9xiu/"+roomId;
		}
		if(rtmpPlayer.start(rtmpUrl)){
//			int screenWidth=mainActivity.getWindowManager().getDefaultDisplay().getWidth();
//			int videoHeight=(int) mainActivity.getResources().getDimension(R.dimen.video_heigth);
//			rtmpPlayer.setVideoViewSize(screenWidth,videoHeight);
		}else{
			Utils.MakeToast(mainActivity, "视频播放失败");
		}
		isPlay = true;
	}
	
	/**
	 * 停止播放
	 */
	public void stopPlay(){
		if(rtmpPlayer!=null){
			rtmpPlayer.stop();
		}
		isPlay = false;
	}

	public boolean isPlay(){
		return isPlay;
	}

	@Override
	public void engineStart() {
    	Log.i("PlayererViewController", "engineStart !!!");
    	Log.v("engist", "engistart");
//    	animationDrawable.stop();
    	ivLoad.setVisibility(View.GONE);
	}

	@Override
	public void engineStop() {
    	Log.i("PlayererViewController", "engineStop !!!");
    	Log.v("ensgon", "engineStop");
//    	animationDrawable.stop();
    	ivLoad.setVisibility(View.GONE);
	}

	@Override
	public void engineError(int errCode, String errString) {
    	Log.e("PlayererViewController", "engineError: " + errCode);
    	Utils.MakeToast(mainActivity, "该房间没有直播");
//    	animationDrawable.stop();
    	ivLoad.setVisibility(View.GONE);
    	mainActivity.getRecomandHost();
	}

	@Override
	public void enginePause() {		
    	Log.i("PlayererViewController", "enginePause !!!");
	}

	@Override
	public void engineResume() {
    	Log.i("PlayererViewController", "engineResume !!!");
	}

}
