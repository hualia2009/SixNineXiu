package com.ninexiu.sixninexiu;

import java.io.FileInputStream;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

/**
 * 用户信息界面
 * @author mao
 *
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {
	private EditText etNickName;
	private TextView tvSex;
	private String[] sexArray;
	private User mUser;
	private TextView tvAddress;
	private Dialog mDilaog;
	private ImageView ivHead;
//	private FinalBitmap bitmapLoad;
	private AsyncImageLoader imageLoader;
	private Random random=new Random();
	private InputMethodManager methodManager;
	private String mProvice="",mCity="";
	private boolean mIsHead;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_user_info_layout);
		imageLoader=new AsyncImageLoader();
		etNickName=(EditText) findViewById(R.id.et_nick_name);
		tvSex=(TextView) findViewById(R.id.tv_user_sex);
		sexArray=getResources().getStringArray(R.array.sex_select);
		tvAddress=(TextView) findViewById(R.id.tv_city);
		tvTitle.setText("编辑资料");
		ivHead=(ImageView) findViewById(R.id.iv_user_head);
		methodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mUser=(User) getIntent().getSerializableExtra("user");
		if(Utils.isNotEmptyString(mUser.getSex())){
		String sex=mUser.getSex();
			if(sex.equals("1")){
				tvSex.setText("男");
			}else{
				tvSex.setText("女");
			}
		
		}else{
			tvSex.setText("男");
		}
		
		if(Utils.isNotEmptyString(mUser.getNickName())){
			etNickName.setText(mUser.getNickName());
		}else{
			etNickName.setText("未填写");
		}
		
		if(Utils.isNotEmptyString(mUser.getProvice())||Utils.isNotEmptyString(mUser.getCity())){
			mProvice=mUser.getProvice();
			mCity=mUser.getCity();
			tvAddress.append(mUser.getProvice());
			tvAddress.append(mUser.getCity());
		}else{
			tvAddress.setText("未填写");
		}
		Bitmap  map=imageLoader.loadBitmap(mUser.getAvatar()+"?"+random.nextInt(100), new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
				ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
			}
		});
		if(map==null){
			ivHead.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.loading_image)));
		}else{
			ivHead.setImageBitmap(Utils.toRoundBitmap(map));
		}
		mIsHead=getIntent().getBooleanExtra("ishead",false);
		if(mIsHead){
			Intent intent=new Intent(this,SelectUserHeadActivity.class);
			startActivityForResult(intent,100);
		}
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.left_bt:
			finish();
			break;
		case R.id.layout_head://头像区域
			Intent intent=new Intent(this,SelectUserHeadActivity.class);
			startActivityForResult(intent,100);
			break;

		case R.id.layout_nickname: //用户昵称
			etNickName.setEnabled(true);
			etNickName.setFocusable(true);
			int length=etNickName.getText().toString().trim().length();
			if(length>0){
				etNickName.setSelection(length);
			}
			methodManager.showSoftInput(etNickName, 0);
			break;
			
		case R.id.layout_sex://性别
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setItems(R.array.sex_select, this);
			builder.show();
			break;
			
		case R.id.layout_city://城市
			Intent intentCity=new Intent(this,SelectCityActivity.class);
			startActivityForResult(intentCity, 200);
			break;
			
		case R.id.bt_modify_userinfo://提交用户修改的信息
			doUpateUserInfoTask();
			break;
		}
	}
	
	/**
	 * 修改用戶信息
	 */
	private void doUpateUserInfoTask() {
		String newName=etNickName.getText().toString().trim();
		if(newName.length()==0){
			Utils.MakeToast(getApplicationContext(), "用户昵称不能为空");
			return ;
		}
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		if(newName.equals("未填写")){
			params.put("nickname", "");
		}else{
			params.put("nickname", newName);
		}
		if(tvSex.getText().equals("男")){
			params.put("gender", "1");
		}else{
			params.put("gender", "2");
		}
		params.put("provice", mProvice);
		params.put("city", mCity);
//		Log.e("uid:token:nickname:provice:city",mUser.getUid()+":"+mUser.getToken()+":"+ newName+":"+mProvice+":"+mCity);
		asyncHttpClient.post(AppConstants.UPDATE_USER_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDilaog=Utils.showProgressDialog(UserInfoActivity.this, "更新中...");
				mDilaog.show();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				//Log.e("UpateContent", content);
				mDilaog.dismiss();
				if(content!=null){
					updateLocalUser(content);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mDilaog.dismiss();
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
	}

	protected void updateLocalUser(String content) {
		try {
			JSONObject jsonObject=new JSONObject(content);
			boolean isSuccess=jsonObject.getBoolean("success");
			if(isSuccess==true){
				Utils.MakeToast(getApplicationContext(), "更新成功");
				JSONObject jsonObject2=jsonObject.getJSONObject("retval");
				if(jsonObject2!=null){
					ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
				}
			}else{
				String msg=jsonObject.optString("msg");
				Utils.MakeToast(getApplicationContext(), msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0:
			tvSex.setText(sexArray[which]);
			break;

		case 1:
			tvSex.setText(sexArray[which]);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==200){
			if(resultCode==RESULT_OK){
				tvAddress.setText("");
				mProvice=data.getStringExtra("provice");
				tvAddress.append(mProvice);
				tvAddress.append("  ");
				mCity=data.getStringExtra("city");
				tvAddress.append(mCity);
			}
		}else if(requestCode==100){
			if(resultCode==RESULT_OK){
				//eyan add 
				final String address=data.getStringExtra("url");
				new Handler().post(new Runnable(){

					@Override
					public void run() {
						updateUserHeader(address);
					}
					
				});
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateUserHeader(final String address) {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(address);
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			params.put("imgstr", fileInputStream);
			asyncHttpClient.post(AppConstants.UPLOAD_USER_HEAD, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
					mDilaog=Utils.showProgressDialog(UserInfoActivity.this, "头像上传中...");
					mDilaog.show();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					mDilaog.dismiss();
					if(content!=null){
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success==true){
								String userHead=jsonObject.optString("retval");
								mUser.setAvatar(userHead);
//								Utils.saveUser(UserInfoActivity.this, mUser);
								Utils.MakeToast(getApplicationContext(), "头像上传成功");
								Bitmap bm=BitmapFactory.decodeFile(address);
								ivHead.setImageBitmap(Utils.toRoundBitmap(bm));
							}else{
								String msg=jsonObject.optString("msg");
								Utils.MakeToast(getApplicationContext(), msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					mDilaog.dismiss();
					Utils.MakeToast(getApplicationContext(), "网络连接超时");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
