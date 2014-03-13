package com.ninexiu.sixninexiu;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.OnlyLoginActivity.LOGINTYPE;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.Utils;
import com.ninexiu.viewpagerindicator.CirclePageIndicator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

/**
 * 启动应用的时候 首页
 * 
 * @author mao
 * 
 */
public class SplashActivity extends Activity {

	private ImageView ivSplash;
	private AnimationDrawable drawable;
	private int localVersion;

	private Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			drawable.stop();
			Intent intent = new Intent(SplashActivity.this, NineShowMainActivity.class);
			startActivity(intent);
			finish();
		};
	};
	private ViewPager viewPager;
	private ArrayList<View> views = new ArrayList<View>();
	private CirclePageIndicator circlePageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		final int currentVersionCode = getVersionCode();
		boolean isFirst = currentVersionCode != MoccaPreferences.ISFIRSTSTART.get()? true : false;
		
		if (isFirst) {
			viewPager = (ViewPager) findViewById(R.id.viewpager_splash);
			circlePageIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator2);
			int[] drawables = { R.drawable.start_1, R.drawable.start_2, R.drawable.start_3,
					R.drawable.start_4 };
			for (int i = 0; i <= 3; i++) {
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setImageResource(drawables[i]);
				views.add(iv);
			}
			View iv = new View(this);
			iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			views.add(iv);

			viewPager.setAdapter(new SplashAdapter());
			circlePageIndicator.setViewPager(viewPager);
			circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					if (arg0 == views.size() - 1) {
						MoccaPreferences.ISFIRSTSTART.put(currentVersionCode);
						viewPager.setCurrentItem(views.size() - 2);
						Intent intent = new Intent(SplashActivity.this, NineShowMainActivity.class);
						startActivity(intent);
						finish();
					}
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		} else {
			ivSplash = (ImageView) findViewById(R.id.iv_splash);
			ivSplash.setVisibility(View.VISIBLE);
			drawable = (AnimationDrawable) ivSplash.getBackground();
			uiHandler.sendEmptyMessageDelayed(100, 2 * 1000);
		}
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		autoLogin();
	}
	
	private int getVersionCode() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (drawable != null) {
				drawable.start();
			}
		}
	}

	private void autoLogin() {
		if (MoccaPreferences.LOGGED.get()) {
			if (MoccaPreferences.LOGINTYPE.get() == LOGINTYPE.NORMALLOGIN.ordinal()) {
				doLoginTask();
			} else if (MoccaPreferences.LOGINTYPE.get() == LOGINTYPE.QQLOGIN.ordinal()) {
				doOpenSourceLoginTask(MoccaPreferences.OPENID.get(), "qq");
			} else if (MoccaPreferences.LOGINTYPE.get() == LOGINTYPE.QQLOGIN.ordinal()) {
				doOpenSourceLoginTask(MoccaPreferences.OPENID.get(), "sina");
			}
		}
	}

	private void doLoginTask() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uname", MoccaPreferences.USERNAME.get());
		params.put("passwd", MoccaPreferences.USERPWD.get());
		params.put("os", 1 + "");
		asyncHttpClient.post(AppConstants.USER_LOGIN_URL, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (!TextUtils.isEmpty(content)) {
					try {
						JSONObject jsonObject = new JSONObject(content);
						boolean isSuccess = jsonObject.getBoolean("success");
						if (isSuccess == true) {
							JSONObject jsonObject2 = jsonObject.getJSONObject("retval");
							ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}

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
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						if (!TextUtils.isEmpty(content)) {
							try {
								JSONObject jsonObject = new JSONObject(content);
								boolean isSuccess = jsonObject.getBoolean("success");
								if (isSuccess == true) {
									JSONObject jsonObject2 = jsonObject.getJSONObject("retval");
									ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Utils.MakeToast(getApplicationContext(), "连接超时");
					}
				});
	}

	class SplashAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			if (position < views.size()) {
				((ViewPager) container).removeView(views.get(position));
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.setAutoLocation(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
