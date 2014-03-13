package com.ninexiu.sixninexiu;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.NLog;
import com.ninexiu.utils.Utils;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.umeng.analytics.MobclickAgent;

public class RegActivity extends Activity {
	private TextView titleText;
	private EditText etUserName,etPwd;
	private Dialog mDialog;
	private boolean mIsFromLive;
	private InputMethodManager methodManager;
	private Button btSumbit;
	private String userName="",pwd="",mOpenId;
	private AuthReceiver receiver;
	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,"
            + "add_share,add_topic";
	
	private Handler uiHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				String opendId=(String) msg.obj;
				doOpenSourceLoginTask(opendId,"qq");
				NLog.e("test", "aaaaa1111");
				break;
				
			case 200:
				String accessToken=(String) msg.obj;
				doOpenSourceLoginTask(accessToken, "sina");
				break;
			}
			
		}; 
	 };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reg_layout);
		btSumbit=(Button) findViewById(R.id.bt_sumbit);
		titleText=(TextView) findViewById(R.id.title_text);
		titleText.setText("注册");
		mIsFromLive=getIntent().getBooleanExtra("fromChatRoom", false);
		etUserName=(EditText) findViewById(R.id.et_input_username);
		etPwd=(EditText) findViewById(R.id.et_input_pwd);
		methodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		getLoginRecorder();
		registerIntentReceivers();
	}
	
	private void registerIntentReceivers() {
		receiver =  new AuthReceiver(uiHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TencentOpenHost.AUTH_BROADCAST);
		registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void onClick(View view){
		switch (view.getId()) {
		case R.id.bt_sumbit:
			doRegTask();
			break;

		case R.id.left_bt:
			finish();
			break;
			
		case R.id.regis_qq_login:
			TencentOpenAPI2.logIn(getApplicationContext(), mOpenId, SCOPE, AppConstants.APP_ID, "_slef", AppConstants.CALL_BACK, null, null);
			break;
			
		case R.id.regis_sina_login:
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			 weibo.setPlatformActionListener(new PlatformActionListener() {
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {
					
				}
				
				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
					String accessToken = arg0.getDb().getToken(); // 获取授权token
//					 String openId = arg0.getDb().getUserId(); // 获取用户在此平台的ID
					uiHandler.sendMessage(uiHandler.obtainMessage(200, accessToken));
				}
				
				@Override
				public void onCancel(Platform arg0, int arg1) {
					
				}
			});
			weibo.authorize();
			break;
		}
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

	/*
	 * 用户注册
	 */
	private void doRegTask() {
		final String userName=etUserName.getText().toString().trim();
		final String pwd=etPwd.getText().toString().trim();
		if(Utils.canRegOrLogin(this, userName, pwd)){
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("username", userName);
			params.put("password", pwd);
			params.put("os", "1");
			asyncHttpClient.post(AppConstants.USER_REG, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
					mDialog=Utils.showProgressDialog(RegActivity.this, "注册中...");
					mDialog.show();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
						try {
							JSONObject jsonObject=new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success==true){
								AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
								RequestParams params=new RequestParams();
								params.put("uname", userName);
								params.put("passwd", pwd);
								params.put("os", 1+"");
								asyncHttpClient.post(AppConstants.USER_LOGIN_URL, params, new AsyncHttpResponseHandler(){
									@Override
									public void onStart() {
										super.onStart();
									}
									
									@Override
									public void onSuccess(String content) {
										super.onSuccess(content);
										mDialog.dismiss();
										if(content!=null){
											try {
												JSONObject jsonObject=new JSONObject(content);
												boolean isSuccess=jsonObject.getBoolean("success");
												if(isSuccess==true){
													JSONObject jsonObject2=jsonObject.getJSONObject("retval");
													if(jsonObject2!=null){
														ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
														ApplicationEx.get().getUserManager().saveUserData(userName, pwd, true);
														if(mIsFromLive){
															Intent intent=new Intent(RegActivity.this,ChatRoomActivity.class);
															intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															startActivity(intent);
															finish();
														}else{
															Intent intent=new Intent(RegActivity.this,NineShowMainActivity.class);
															intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															startActivity(intent);
															finish();
														}
													}
												}else{
													String msg=jsonObject.optString("msg");
													Utils.MakeToast(getApplicationContext(), msg);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											
										}
									}
									
									@Override
									public void onFailure(Throwable error, String content) {
										super.onFailure(error, content);
										Utils.MakeToast(getApplicationContext(), "网络连接超时");
										mDialog.dismiss();
									}
								});
							}else{
								mDialog.dismiss();
								String msg=jsonObject.optString("msg");
								Utils.MakeToast(RegActivity.this, msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					mDialog.dismiss();
					Utils.MakeToast(RegActivity.this, "网络超时");
				}
			});
		}
	}
	
	/**
	 * 保存用户登录的信息
	 */
	protected void SaveUserLoginInfo(String content) {
		NLog.e("test", "1111");
		try {
			JSONObject jsonObject=new JSONObject(content);
			boolean isSuccess=jsonObject.getBoolean("success");
			if(isSuccess==true){
				JSONObject jsonObject2=jsonObject.getJSONObject("retval");
				if(jsonObject2!=null){
					ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
					ApplicationEx.get().getUserManager().saveUserData(userName, pwd, true);
					if(mIsFromLive){
						Intent intent=new Intent(RegActivity.this,ChatRoomActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}else{
						Intent intent=new Intent(RegActivity.this,NineShowMainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}
				
			}else{
				String msg=jsonObject.optString("msg");
				Utils.MakeToast(getApplicationContext(), msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 用户用QQ登录
	 */
	private void doOpenSourceLoginTask(String opendId,String source) {
		
		NLog.e("test", "0000000");
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("openid", opendId);
		params.put("source", source);
		params.put("os", 1+"");
		asyncHttpClient.post(AppConstants.OPEN_SOURCE_LOGIN, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(RegActivity.this, "登录中...");
				mDialog.show();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if(content!=null){
				//	Log.e("content", content);
					SaveUserLoginInfo(content);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mDialog.dismiss();
				Utils.MakeToast(getApplicationContext(), "连接超时");
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			try {
				methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
