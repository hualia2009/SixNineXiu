package com.ninexiu.sixninexiu;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.Utils;

public class ModiyPwdActivity extends BaseActivity {

	private EditText editText;
	private EditText etNew;
	private EditText etNewAgin;
	private String oldPwd, newPwd, etNewAgain;
	private User mUser;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_pwd);
		mUser = ApplicationEx.get().getUserManager().getUser();
		editText = (EditText) findViewById(R.id.et_old_pwd);
		etNew = (EditText) findViewById(R.id.et_new_pwd);
		etNewAgin = (EditText) findViewById(R.id.et_new_pwd_twice);
		tvTitle.setText("修改密码");
	}

	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.bt_ok:
			oldPwd = editText.getText().toString().trim();
			newPwd = etNew.getText().toString().trim();
			etNewAgain = etNewAgin.getText().toString().trim();
			if (Utils.isNotEmptyString(oldPwd)) {
				if (Utils.isNotEmptyString(newPwd)) {
					if (Utils.isNotEmptyString(etNewAgain)) {
						if (newPwd.equals(etNewAgain)) {
							doMofiyTask();
						} else {
							Utils.MakeToast(getApplicationContext(), "两次输入的密码不一致");
						}
					} else {
						Utils.MakeToast(getApplicationContext(), "请再次输入新密码");
					}
				} else {
					Utils.MakeToast(getApplicationContext(), "请输入新密码");
				}
			} else {
				Utils.MakeToast(getApplicationContext(), "请输入旧密码");
			}
			break;

		default:
			break;
		}
	}

	private void doMofiyTask() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("oldpass", oldPwd);
		params.put("newpass", newPwd);
		params.put("newpassre", etNewAgain);
		asyncHttpClient.post(AppConstants.MODIFY_PWD, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				mDialog = Utils.showProgressDialog(ModiyPwdActivity.this, "处理中...");
				mDialog.show();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if (content != null) {
					try {
						JSONObject jsonObject = new JSONObject(content);
						boolean success = jsonObject.optBoolean("success");
						if (success) {
							Utils.MakeToast(getApplicationContext(), "修改成功");
							MoccaPreferences.USERPWD.put(newPwd);
							finish();
						} else {
							String msg = jsonObject.optString("msg");
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
				mDialog.dismiss();
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});

	}

}
