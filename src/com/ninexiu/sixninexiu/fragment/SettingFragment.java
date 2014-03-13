package com.ninexiu.sixninexiu.fragment;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.AboutActivity;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ModiyPwdActivity;
import com.ninexiu.sixninexiu.OnlyLoginActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.UpdateService;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.IUserManager;
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

/*
 * 设置界面
 */
public class SettingFragment extends BaseFragment {
	private Button btVersion;
	private User mUser;
	private Dialog mDialog;
	private int localVersion;// 本地的版本号
	private String url;
	private Button btLoginOut;
	private IUserManager iUserManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		iUserManager = new IUserManager() {
			
			@Override
			public void IUserStateChanged() {
				mUser = ApplicationEx.get().getUserManager().getUser();
				if (mUser != null) {
					btLoginOut.setText("注销");
				} else {
					btLoginOut.setText("登录");
				}
			}
		};
		ApplicationEx.get().getUserManager().add(iUserManager);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_layout, null);
		btVersion = (Button) view.findViewById(R.id.bt_version);
		btLoginOut = (Button) view.findViewById(R.id.bt_login_out);
		mUser = ApplicationEx.get().getUserManager().getUser();
		if (mUser != null) {
			btLoginOut.setText("注销");
		} else {
			btLoginOut.setText("登录");
		}
		PackageInfo packageInfo;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
			localVersion = packageInfo.versionCode;
			btVersion.setText(packageInfo.versionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.findViewById(R.id.layout_modify_pwd).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.layout_rank).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.layout_feedback).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.layout_check_update).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.layout_about).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.bt_login_out).setOnClickListener(mOnClickListener);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ApplicationEx.get().getUserManager().remove(iUserManager);
	}

	@Override
	public int getTopTitle() {
		return R.string.setting;
	}

	private void init() {
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());
		mUser=ApplicationEx.get().getUserManager().getUser();
		if(mUser!=null){
			btLoginOut.setText("注销");
		}else{
			btLoginOut.setText("登录");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_modify_pwd: // 修改密码
				if (mUser != null) {
					Intent modifyPwd = new Intent(getActivity(), ModiyPwdActivity.class);
					startActivity(modifyPwd);
				} else {
					// Intent intent=new Intent(this,LoginActivity.class);
					// startActivity(intent);
					// Utils.showLoginDialog(this);
					Utils.MakeToast(getActivity(), "请先登录");
				}
				break;
			case R.id.layout_rank:// 打分评价
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + getActivity().getPackageName()));
				startActivity(intent);
				break;
			case R.id.layout_feedback:// 意见反馈
				FeedbackAgent agent = new FeedbackAgent(getActivity());
				agent.startFeedbackActivity();
				break;

			case R.id.layout_check_update:// 检查更新
				CheckUpdate();
				break;

			case R.id.layout_about: // 关于本软件
				Intent intentAbout = new Intent(getActivity(), AboutActivity.class);
				startActivity(intentAbout);
				break;

			case R.id.bt_login_out: // 退出按钮
				if (mUser != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("提醒");
					builder.setMessage("是否确认退出?");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MoccaPreferences.LOGGED.put(false);
							ApplicationEx.get().getUserManager().setUser(null);
							ApplicationEx.get().getUserManager().notifyState();
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				} else {
					Intent intents = new Intent(getActivity(), OnlyLoginActivity.class);
					startActivity(intents);
				}
				break;
			}
		}
	};

	/**
	 * 检测更新
	 */
	private void CheckUpdate() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("os", "1");
		asyncHttpClient.post(AppConstants.CHECK_UPDATE, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				mDialog = Utils.showProgressDialog(getActivity(), "检测更新中...");
				mDialog.show();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if (content != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success = jsonObject.optBoolean("success");
						if (success) {
							JSONObject jsonObject2 = jsonObject.optJSONObject("retval");
							String code = jsonObject2.optString("versionCode");
							url = jsonObject2.optString("url");
							// Log.e("test", "version_url"+url);

							if (Utils.isNotEmptyString(code)) {
								if (Float.valueOf(code) > localVersion) {
									AlertDialog.Builder builder2 = new AlertDialog.Builder(
											getActivity());
									builder2.setTitle("更新");
									builder2.setMessage("发现新版本,建议立即更新?");
									builder2.setPositiveButton("确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,
														int which) {
													Intent intent = new Intent(getActivity(),
															UpdateService.class);
													Bundle bundle = new Bundle();
													bundle.putString("Key_App_Name",
															getString(R.string.app_name));
													bundle.putString("Key_Down_Url", url);
													intent.putExtras(bundle);
													// intent.putExtra("Key_App_Name","九秀美女直播");
													// intent.putExtra("Key_Down_Url",url);
													getActivity().startService(intent);
												}
											});
									builder2.setNegativeButton("取消", null);
									builder2.show();
								} else {
									Utils.MakeToast(getActivity(), "已是最新版本");
								}

							}
						} else {
							String msg = jsonObject.optString("msg");
							Utils.MakeToast(getActivity(), msg);
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
				Utils.MakeToast(getActivity(), "网络连接超时");
			}
		});

	}

}
