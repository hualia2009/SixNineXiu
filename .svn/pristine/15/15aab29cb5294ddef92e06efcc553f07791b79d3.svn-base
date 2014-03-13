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
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.Utils;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
import com.umeng.analytics.MobclickAgent;

//implements IUiListener
public class OnlyLoginActivity extends Activity {
	/** 登录方式 */
	public enum LOGINTYPE {
		NORMALLOGIN, QQLOGIN, SINALOGIN
	}

	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,"
			+ "add_share,add_topic";
	// private Tencent mTencent;
	private EditText etUserName;
	private EditText etPwd;
	private Dialog mDialog;
	private boolean mIsFromLive = false;
	private InputMethodManager methodManager;
	private String userName = "", pwd = "", mOpenId;
	private AuthReceiver receiver;
	// private SsoHandler mSsoHandler;

	private Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				String opendId = (String) msg.obj;
				MoccaPreferences.LOGINTYPE.put(LOGINTYPE.QQLOGIN.ordinal());
				MoccaPreferences.OPENID.put(opendId);
				doOpenSourceLoginTask(opendId, "qq");
				break;

			case 200:
				String accessToken = (String) msg.obj;
				MoccaPreferences.LOGINTYPE.put(LOGINTYPE.SINALOGIN.ordinal());
				MoccaPreferences.OPENID.put(accessToken);
				doOpenSourceLoginTask(accessToken, "sina");
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_only);
		((TextView) findViewById(R.id.title_text)).setText("登录");
		methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		etUserName = (EditText) findViewById(R.id.et_input_username);
		etPwd = (EditText) findViewById(R.id.et_input_pwd);
		Intent intent = getIntent();
		mIsFromLive = intent.getBooleanExtra("fromChatRoom", false);
		etUserName.setText(MoccaPreferences.USERNAME.get());
		etPwd.setText(MoccaPreferences.USERPWD.get());
		registerIntentReceivers();
	}

	private void registerIntentReceivers() {
		receiver = new AuthReceiver(uiHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(TencentOpenHost.AUTH_BROADCAST);
		registerReceiver(receiver, filter);
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

	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.bt_register:
			intent = new Intent(this, RegActivity.class);
			intent.putExtra("fromChatRoom", mIsFromLive);
			startActivity(intent);
			break;

		case R.id.bt_login:
			doLoginTask();
			break;

		case R.id.left_bt:
			finish();
			break;

		case R.id.qq_login:
			TencentOpenAPI2.logIn(getApplicationContext(), mOpenId, SCOPE, AppConstants.APP_ID,
					"_slef", AppConstants.CALL_BACK, null, null);
			break;

		case R.id.sina_login:
			// doWeiboLoginTask();
			Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			weibo.setPlatformActionListener(new PlatformActionListener() {
				@Override
				public void onError(Platform arg0, int arg1, Throwable arg2) {

				}

				@Override
				public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
					String accessToken = arg0.getDb().getToken(); // 获取授权token
					// String openId = arg0.getDb().getUserId(); // 获取用户在此平台的ID
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

	/*
	 * 手机用户登录
	 */
	private void doMobileLogin() {
		userName = etUserName.getText().toString().trim();
		pwd = etPwd.getText().toString().trim();
		if (userName.length() == 0) {
			Utils.MakeToast(getApplicationContext(), "请输入手机号码");
			return;
		} else if (pwd.length() == 0) {
			Utils.MakeToast(getApplicationContext(), "请输入密码");
			return;
		} else if (pwd.length() < 6) {
			Utils.MakeToast(getApplicationContext(), "密码必须六位以上");
			return;
		} else {

		}
	}

	// 处理用户登录
	private void doLoginTask() {
		userName = etUserName.getText().toString().trim();
		pwd = etPwd.getText().toString().trim();
		if (Utils.canRegOrLogin(this, userName, pwd)) {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("uname", userName);
			params.put("passwd", pwd);
			params.put("os", 1 + "");
			asyncHttpClient.post(AppConstants.USER_LOGIN_URL, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
							mDialog = Utils.showProgressDialog(OnlyLoginActivity.this, "登录中....");
							mDialog.show();
						}

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							mDialog.dismiss();
							if (content != null) {
								SaveUserLoginInfo(content);
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							mDialog.dismiss();
							Utils.MakeToast(getApplicationContext(), "网络连接超时");
						}
					});
		}
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
				if (jsonObject2 != null) {
					ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
					MoccaPreferences.LOGGED.put(true);
					ApplicationEx.get().getUserManager().saveUserData(userName, pwd, true);
					if (mIsFromLive) {
						Intent intent = new Intent(OnlyLoginActivity.this, ChatRoomActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					} else {
						Intent intent = new Intent(OnlyLoginActivity.this,
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
						mDialog = Utils.showProgressDialog(OnlyLoginActivity.this, "登录中...");
						mDialog.show();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mDialog.dismiss();
						if (content != null) {
							// Log.e("content", content);
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
				methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterIntentReceivers();
		}
	}

	private void unregisterIntentReceivers() {
		unregisterReceiver(receiver);
	}
}
