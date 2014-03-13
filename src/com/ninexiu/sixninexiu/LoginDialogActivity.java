package com.ninexiu.sixninexiu;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.NLog;
import com.ninexiu.utils.Utils;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.umeng.analytics.MobclickAgent;

/**
 * 如果用户没有登录 弹出要求用户登录框
 * 
 * @author mao
 * 
 */
public class LoginDialogActivity extends Activity {

	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,"
			+ "add_share,add_topic";
	private String mOpenId;
	private boolean fromChatRoom;
	Dialog mDialog;
	private AuthReceiver receiver;

	private Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				String opendId = (String) msg.obj;
				NLog.e("test", "QQopenID==" + opendId);
				MoccaPreferences.OPENID.put(opendId);
				doOpenSourceLoginTask(opendId, "qq");
				break;

			case 200:
				String accessToken = (String) msg.obj;
				MoccaPreferences.OPENID.put(accessToken);
				doOpenSourceLoginTask(accessToken, "sina");
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog_layout);
		fromChatRoom = getIntent().getBooleanExtra("fromChatRoom", false);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		registerIntentReceivers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void registerIntentReceivers() {
		receiver = new AuthReceiver(uiHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(TencentOpenHost.AUTH_BROADCAST);
		registerReceiver(receiver, filter);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_login:
			Intent intent = new Intent(this, OnlyLoginActivity.class);
			intent.putExtra("fromChatRoom", fromChatRoom);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_login_qq:
			TencentOpenAPI2.logIn(getApplicationContext(), mOpenId, SCOPE, AppConstants.APP_ID,
					"_slef", AppConstants.CALL_BACK, null, null);
			break;

		default:
			break;
		}
	}

	/**
	 * 用户用QQ登录
	 */
	private void doOpenSourceLoginTask(String opendId, String source) {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("openid", opendId);
		params.put("source", source);
		params.put("os", 1 + "");
		asyncHttpClient.post(AppConstants.OPEN_SOURCE_LOGIN, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						mDialog = Utils.showProgressDialog(LoginDialogActivity.this, "登录中...");
						mDialog.show();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mDialog.dismiss();
						if (content != null) {
							SaveUserLoginInfo(content);
							finish();
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

	/**
	 * 保存用户登录的信息
	 */
	protected void SaveUserLoginInfo(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			boolean isSuccess = jsonObject.getBoolean("success");
			if (isSuccess == true) {
				JSONObject jsonObject2 = jsonObject.getJSONObject("retval");
				NLog.e("test", "QQ登录返回用户信息：" + jsonObject2.toString());
				if (jsonObject2 != null) {
					ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
					MoccaPreferences.LOGGED.put(true);
					if (fromChatRoom) {
						Intent intent = new Intent(LoginDialogActivity.this, ChatRoomActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					} else {
						Intent intent = new Intent(LoginDialogActivity.this,
								NineShowMainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				}

			} else {
				String msg = jsonObject.optString("msg");
				Utils.MakeToast(getApplicationContext(), msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

}
