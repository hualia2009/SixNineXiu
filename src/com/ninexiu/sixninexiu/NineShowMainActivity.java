package com.ninexiu.sixninexiu;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import cn.sharesdk.framework.ShareSDK;

import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.fragment.BaseFragment;
import com.ninexiu.sixninexiu.fragment.ContentFragment;
import com.ninexiu.sixninexiu.fragment.LeftMenuFragment;
import com.ninexiu.sixninexiu.fragment.RightMenuFragment;
import com.ninexiu.sixninexiu.fragment.RightMenuFragment.HostSelectedListener;
import com.ninexiu.slidingmenu.SlidingFragmentActivity;
import com.ninexiu.slidingmenu.SlidingMenu;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/*
 * 显示直播大厅的页面
 */
public class NineShowMainActivity extends SlidingFragmentActivity implements HostSelectedListener{
	
	public  SlidingMenu slidingMenu;
	private boolean mIsLogin=false;
	private int localVersion;
//	private Dialog mDialog;
	private String url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));	
		initSlingMenu();
		ShareSDK.initSDK(this);
		
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			localVersion=packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//检测更新
		CheckUpdate();
		Log.v("Tag", "localVersion=="+localVersion);
		//Log.e("test","onCreate...");
	}
	
	private void initSlingMenu() {
		setBehindContentView(R.layout.menu_frame);
		slidingMenu=getSlidingMenu();
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setFadeDegree(0.45f);
//		slidingMenu.setBehindWidth(130);
		slidingMenu.setBehindScrollScale(0f);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		slidingMenu.setBehindWidthRes(R.dimen.behind_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setContentView(R.layout.content_frame);
//		SharedPreferences preferences=getSharedPreferences(AppConstants.LOGIN_STATUS, MODE_PRIVATE);
//		if(preferences.getBoolean(AppConstants.IS_LOGIN, false)==true){
//			mIsLogin=true;
			slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
			slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
			slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
			//开启右边事物
//			slidingMenu.setRightBehindWidthRes(R.dimen.sliding_right);
//			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,rightMenuFragment).commit();
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,new RightMenuFragment(new IRightTabSelected() {
				
				@Override
				public void setSelected(String id) {
					showFragment(ContentFragment.class, id);
					if(slidingMenu.isMenuShowing()){
						slidingMenu.showContent();
					}else{
						slidingMenu.showMenu(true);
					}
				}
			})).commit();
//		}else{
//			slidingMenu.setMode(SlidingMenu.LEFT);
//		}
		//开启左边事物
//		setBehindContentView(R.layout.left_meun_layout);
		FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//		fragmentTransaction.replace(R.id.menu_frame, new LeftMenuFragment()).commit();
		fragmentTransaction.replace(R.id.menu_frame, new LeftMenuFragment(new ITabSelected() {
			
			@Override
			public void setSelected(Class<? extends BaseFragment> c) {
				showFragment(c);
				if(slidingMenu.isMenuShowing()){
					slidingMenu.showContent();
				}else{
					slidingMenu.showMenu(true);
				}
			}
		})).commit();
		//开启中间事物
//		setContentView(R.layout.content_frame);
		showFragment(ContentFragment.class);
	}

	private Map<String, BaseFragment> map = new HashMap<String, BaseFragment>();
	private Stack<String> mStack = new Stack<String>();

	private void showFragment(Class<? extends BaseFragment> c) {
		showFragment(c, "");
	}
	
	/**
	 * 显示新的Fragment
	 * @param c
	 * @param type 直播大厅页面的主播类型
	 */
	private void showFragment(Class<? extends BaseFragment> c, String type) {
		try {
			BaseFragment fragment;
			// 如果mStack大于0，则隐藏
			if(mStack.size() > 0) {
				getSupportFragmentManager().beginTransaction().hide(map.get(mStack.pop())).commit();
			}
			mStack.add(c.getSimpleName());
			// 如果已经存在，则显示之前的，如果不存在，则创建新的
			if (map.containsKey(c.getSimpleName())) {
				fragment = map.get(c.getSimpleName());
				if (!TextUtils.isEmpty(type)) {
					Bundle bundle = new Bundle();
					bundle.putString(ContentFragment.PARAMANCHORTYPE, type);
					fragment.setBundle(bundle);
				}
				getSupportFragmentManager().beginTransaction().show(fragment).commit();
			} else {
				fragment = c.newInstance();
				map.put(c.getSimpleName(), fragment);
				getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, c.getSimpleName()).commit();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	//---onClick
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.left_bt://点击大厅最左边的按钮
			if(slidingMenu.isMenuShowing()){
				slidingMenu.showContent();
			}else{
				slidingMenu.showMenu(true);
			}
			break;

		case R.id.right_bt://点击大厅最右边的按钮
//			if(!mIsLogin){
//				Utils.showLoginDialog(this,false);
//			}else{
				if(slidingMenu.isSecondaryMenuShowing()){
					slidingMenu.showContent();
				}else{
					slidingMenu.showSecondaryMenu(true);
				}
//			}
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
//		SharedPreferences preferences=getSharedPreferences(AppConstants.LOGIN_STATUS, MODE_PRIVATE);
//		if(preferences.getBoolean(AppConstants.IS_LOGIN, false)==false){
//			mIsLogin=false;
//			getSupportFragmentManager().beginTransaction().remove(rightMenuFragment);
//			rightMenuFragment=null;
//			slidingMenu.setMode(SlidingMenu.LEFT);
//			slidingMenu.removeView(slidingMenu.getSecondaryMenu());
//		}else{
//			if(rightMenuFragment==null){
//				mIsLogin=true;
//				slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
//				slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
//				slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
//				rightMenuFragment=new RightMenuFragment();
//				//开启右边事物
//			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,rightMenuFragment).commit();
//			}
//		}
		//if(rightMenuFragment != null)
		//rightMenuFragment.refreshCoins();
	}

	public void showFragment(int tag){
		if(slidingMenu.isMenuShowing())
			slidingMenu.showContent();
//		contentFragment.showFragment(tag);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		SharedPreferences preferences=getSharedPreferences(AppConstants.LOGIN_STATUS, MODE_PRIVATE);
//		if(preferences.getBoolean(AppConstants.IS_LOGIN, false)==true){
//			mIsLogin=true;
			slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
			slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
			slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
//			rightMenuFragment=new RightMenuFragment();
			//开启右边事物
//			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,rightMenuFragment).commitAllowingStateLoss();
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two,new RightMenuFragment(new IRightTabSelected() {
				
				@Override
				public void setSelected(String id) {
					
				}
			})).commitAllowingStateLoss();
			if(slidingMenu.isMenuShowing()){
				slidingMenu.showContent();
			}
//		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				Log.v("onkeyDown", "keydown");
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("温馨提示");
				builder.setMessage("退出后您可以通过电脑观看\nwww.69xiu.com");
				builder.setPositiveButton("退出程序", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.show();
				return true;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 检测更新
	 */
	 private void CheckUpdate() {
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("os", "1");
			asyncHttpClient.post(AppConstants.CHECK_UPDATE, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
	//				mDialog=Utils.showProgressDialog(NineShowMainActivity.this, "检测更新中...");
	//				mDialog.show();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
	//				mDialog.dismiss();
					if(content!=null){
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success){
								JSONObject jsonObject2=jsonObject.optJSONObject("retval");
								String code=jsonObject2.optString("versionCode");
								final String versionName = jsonObject2.optString("versionName");
								url=jsonObject2.optString("url");
//								Log.e("Tag", "version_url "+url);
								Log.v("Tag", "code=="+code);
								Log.v("Tag", "version name "+versionName);
	
								//localVersion = 1;
								if(Utils.isNotEmptyString(code)){
									if(Float.valueOf(code)>localVersion){
									Dialog dialog = new AlertDialog.Builder(
											NineShowMainActivity.this)
											.setTitle(versionName + "版本更新")
											.setMessage(jsonObject2.optString("content"))
											.setPositiveButton("确定",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface dialog,
																int which) {
//															showUpdatingDialog();
															showDownloadProgress();
															Intent intent = new Intent(
																	NineShowMainActivity.this,
																	UpdateService.class);
															Bundle bundle = new Bundle();
															bundle.putString("Key_App_Name",
																	getString(R.string.app_name));
															intent.putExtra("receiver", new DownloadReceiver(new Handler()));
															bundle.putString("Key_Down_Url", url);
															bundle.putString("version_name", versionName);
															intent.putExtras(bundle);
															// intent.putExtra("Key_App_Name","九秀美女直播");
															// intent.putExtra("Key_Down_Url",url);
															startService(intent);
														}
													}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
														
														@Override
														public void onClick(DialogInterface dialog, int which) {
															finish();
														}
													}).create();
									dialog.setOnKeyListener(new OnKeyListener() {
										int i;// 用于控制按下和抬起动作

										@Override
										public boolean onKey(DialogInterface dialog1, int keyCode,
												KeyEvent event) {
											i++;
											if (i == 2 && keyCode == KeyEvent.KEYCODE_BACK)
												return false;
											return true;
										}
									});
									dialog.setCanceledOnTouchOutside(false);
									dialog.show();
									}else{
	//									Utils.MakeToast(getApplicationContext(), "已是最新版本");
									}
								}
							}else{
								String msg=jsonObject.optString("msg");
//								Utils.MakeToast(getApplicationContext(), msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
	//				mDialog.dismiss();
					Utils.MakeToast(getApplicationContext(), "网络连接超时");
				}
			});
	}

	 private void showUpdatingDialog() {
		 Dialog dialog = new AlertDialog.Builder(
					NineShowMainActivity.this)
					.setTitle("版本更新")
					.setMessage("正在更新...")
					.create();
			dialog.setOnKeyListener(new OnKeyListener() {
				int i;// 用于控制按下和抬起动作

				@Override
				public boolean onKey(DialogInterface dialog1, int keyCode,
						KeyEvent event) {
					i++;
					if (i == 2 && keyCode == KeyEvent.KEYCODE_BACK)
						return false;
					return true;
				}
			});
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
	 }
	 
	private class DownloadReceiver extends ResultReceiver {
		public DownloadReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, final Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == UpdateService.UPDATE_PROGRESS) {
				int progress = resultData.getInt("progress");
				if (mpDialog != null) {
					if (!mpDialog.isShowing()) {
						mpDialog.show();
					}
					mpDialog.setProgress(progress);
					if (progress >= 100) {
						mpDialog.dismiss();
						showDownloadedDialog(resultData.getString("update_file_path"));
					}
				}
			}
		}
	}
	 ProgressDialog mpDialog;
	 private void showDownloadProgress() {
		 mpDialog = new ProgressDialog(this);  
         mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
         mpDialog.setTitle("版本更新");  
         mpDialog.setMessage("正在更新");  
         mpDialog.setMax(100);  
         mpDialog.setProgress(0);  
         mpDialog.setIndeterminate(false);  
         mpDialog.setCancelable(false);  
         mpDialog.setOnKeyListener(new OnKeyListener() {
				int i;// 用于控制按下和抬起动作

				@Override
				public boolean onKey(DialogInterface dialog1, int keyCode,
						KeyEvent event) {
					i++;
					if (i == 2 && keyCode == KeyEvent.KEYCODE_BACK)
						return false;
					return true;
				}
			});
         mpDialog.setCanceledOnTouchOutside(false);
         mpDialog.show();
	 }
	 
	 /**
	  * 显示下载完成的框
	  */
	 private void showDownloadedDialog(final String filePath) {
		 Dialog dialog = new AlertDialog.Builder(
					NineShowMainActivity.this)
					.setTitle("版本更新")
					.setMessage("下载完成，请安装！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Field field = dialog
												.getClass()
												.getSuperclass()
												.getDeclaredField(
														"mShowing");
										field.setAccessible(true);
										field.set(dialog, false);
									} catch (Exception e) {
										e.printStackTrace();
									}
									Intent intent2 = new Intent();
									intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent2.setAction(android.content.Intent.ACTION_VIEW);
									intent2.setDataAndType(Uri.fromFile(new File(filePath)), 
											"application/vnd.android.package-archive");
									startActivity(intent2);
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							}).create();
			dialog.setOnKeyListener(new OnKeyListener() {
				int i;// 用于控制按下和抬起动作

				@Override
				public boolean onKey(DialogInterface dialog1, int keyCode,
						KeyEvent event) {
					i++;
					if (i == 2 && keyCode == KeyEvent.KEYCODE_BACK)
						return false;
					return true;
				}
			});
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
	 }
	 
	@Override
	public void hostTypeSelected(int type) {
		// TODO Auto-generated method stub
//		contentFragment.setHostAdapter(type);
	}
	
	public interface IRightTabSelected {
		public void setSelected(String id);
	}
	/**
	 * 左右控制回调
	 */
	public interface ITabSelected {
		public void setSelected(Class<? extends BaseFragment> c);
	}
}
