package com.ninexiu.sixninexiu;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.NLog;
import com.ninexiu.utils.Utils;

/*
 * 输入充值卡界面
 */
public class MobileCardInputActivity extends BaseActivity {
	private EditText etCard,etPwd;
	private Dialog mDialog;
	private String cardType;
	private int chargeMoney;
	private String cardNum,cardPwd;
	private  User mUser;
	private boolean mFromTask;
	private String taskId;
	private String taskId2;
	private Timer timer;
	private TimerTask task;
	private String orderID;
	private int code, count;

	private  Handler uiHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
//			Utils.UpdateUserInfo(mUser, MobileCardInputActivity.this);
			switch (msg.what) {
			case 1:
				if(count++<22) {
					doRechargeTask();
				}else {
					mDialog.dismiss();
					Utils.MakeToast(getApplicationContext(), "充值失败");
				}
				if (count==21) {
					timer.cancel();
				}
				break;

			case 2:
				timer.schedule(task, 1000, 3000);
				break;
			
			case 3:
				if (code==100) {
					mDialog.dismiss();
					Utils.MakeToast(getApplicationContext(), "充值成功！");
					Utils.UpdateUserInfo(mUser, MobileCardInputActivity.this);
					
				}
				 if (code==1) {
					mDialog.dismiss();
					
					Utils.MakeToast(getApplicationContext(), "充值失败");
				}
				 timer.cancel();
				 
				 break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_input_mobile_card_layout);
		etCard=(EditText) findViewById(R.id.et_card_num);
		etPwd=(EditText) findViewById(R.id.et_card_pwd);
		tvTitle.setText("手机充值卡");
		Intent intent=getIntent();
		cardType=intent.getStringExtra("cardType");
		chargeMoney=intent.getIntExtra("chargeMoney", 0);
		mUser=(User) intent.getSerializableExtra("user");
		mFromTask=intent.getBooleanExtra("fromtask", false);
		taskId=intent.getStringExtra("taskid");
		taskId2=intent.getStringExtra("taskid2");
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 1;
				uiHandler.sendMessage(message);
			}
		};
	}
	
	public void onClick(View view){
		super.onClick(view);
		switch (view.getId()) {
		case R.id.bt_ok:
			cardNum=etCard.getText().toString().trim();
			cardPwd=etPwd.getText().toString().trim();
			if(cardNum.length()==0){
				Utils.MakeToast(getApplicationContext(), "请输入充值卡号");
			}else if(cardPwd.length()==0){
				Utils.MakeToast(getApplicationContext(), "请输入充值卡密码");
			}else{
				doMobileCardChargeTask();
			}
			break;

		default:
			break;
		}
		
	}
	/**
	 * {
    "success": true,
    "msg": "执行成功!",
    "retval": {
        "success": true,
        "msg": "提交成功!",
        "retInfo": "",
        "orderSN": "I13794106734936",
        "code": "1",
        "page": "r0_Cmd=ChargeCardDirect\nr1_Code=1\nr6_Order=I13794106734936\nrq_ReturnMsg=\nhmac=eecf318b1c5264245276089894b2aa61\n"
    }
}
	 */
	
	private void doMobileCardChargeTask() {
		if(Utils.isMobileCardOk(cardType, cardNum, cardPwd)){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("amount", chargeMoney+"");
		params.put("pa8_cardNo",cardNum);
		params.put("pa9_cardPwd", cardPwd);
		params.put("pd_FrpId", cardType);
		NLog.e("test", "params=="+params.toString());
		asyncHttpClient.post(AppConstants.MOBILE_CHONG_ZHI, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(MobileCardInputActivity.this, "充值中...");
				mDialog.show();
			}
			
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
//				mDialog.dismiss();
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						orderID = jsonObject.optString("orderid");
						NLog.e("test", "content=="+content.toString());
						if(success){
							Utils.MakeToast(getApplicationContext(), "订单已提交成功，请稍等1分钟，不要离开本页面");
//							uiHandler.sendEmptyMessageDelayed(100, 1000*20);
							
							Message message = new Message();
							message.what = 2;
							uiHandler.sendMessage(message);
							
//							finish();
							 if(mFromTask){
									if(taskId2!=null){
										if(chargeMoney>=500){
											Utils.doUserTask(mUser, taskId,null);
											Utils.doUserTask(mUser, taskId2, null);
										}else{
											Utils.doUserTask(mUser, taskId2, null);
										}
									}else{
										Utils.doUserTask(mUser, taskId,null);
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
				mDialog.dismiss();
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
			
		});
		}else{
			Utils.MakeToast(getApplicationContext(), "充值卡号或密码不对");
		}
	}
	/*
	 * (non-Javadoc)
	 * 二次获取卡类充值状态
	 */
	private void doRechargeTask(){
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderid", orderID);
		NLog.e("test", "orderID=="+orderID);
		NLog.e("test", "testurl=="+AppConstants.MOBILE_RECHARGE+"&"+"orderid="+orderID);
		httpClient.post(AppConstants.MOBILE_RECHARGE, requestParams, new AsyncHttpResponseHandler(){

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				NLog.e("test", "secondcontent=="+content.toString());
				try {
					JSONObject jsonObject = new JSONObject(content);
					code = jsonObject.getInt("code");
					NLog.e("test", "二次请求返回的code=="+code);
					if (code==100||code==1) {
						Message message = new Message();
						message.what =3;
						uiHandler.sendMessage(message);
					}
//					else {
//						Message message = new Message();
//						message.what =1;
//						uiHandler.sendMessage(message);
//					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
			
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(mFromTask){
			Utils.UpdateUserInfo(mUser, this);
//		}
	}

}
