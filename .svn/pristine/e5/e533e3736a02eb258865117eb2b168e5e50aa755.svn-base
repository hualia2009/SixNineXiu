package com.ninexiu.sixninexiu;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninexiu.airpay.AlixId;
import com.ninexiu.airpay.BaseHelper;
import com.ninexiu.airpay.MobileSecurePayHelper;
import com.ninexiu.airpay.MobileSecurePayer;
import com.ninexiu.airpay.PartnerConfig;
import com.ninexiu.airpay.ResultChecker;
import com.ninexiu.airpay.Rsa;
import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;
import com.unionpay.UPPayAssistEx;

/*
 * 支付宝界面
 */
public class ZhifuBaoActivity extends BaseActivity {

	private EditText editTextInput;
	private ProgressDialog mProgress;
	private static final String TAG = "ZhifuBaoActivity";
	private String orderSubject, order_sn, amount, notify_url;
	private TextView tvAccount, tvMoney;
	private User mUser;
	private boolean IsFromMobile;
	private String mMode = "00";
	private static final int PLUGIN_VALID = 0;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;
	private boolean mFromTask=false;
	private String taskId;
	private String taskId2;
	private Dialog mDialog;
	private int ChargeMoney;//充值金额
	private TextView tvThousnd;
	private int[] tvIds=new int[]{R.id.tv_charge_thiry,R.id.tv_charge_fifty,R.id.tv_charge_hundred
			,R.id.tv_charge_thiry_hundred,R.id.tv_charge_five_hundred,R.id.tv_charge_thousand};
	private TextView[] tv=new TextView[tvIds.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhi_fu_bao_layout);
		Intent intent=getIntent();
		IsFromMobile=intent.getBooleanExtra("mobile",false);
		mFromTask=intent.getBooleanExtra("fromtask", false);
		taskId=intent.getStringExtra("taskid");
		taskId2=intent.getStringExtra("taskid2");
		if(IsFromMobile){
			tvTitle.setText("手机支付");
			tvThousnd=(TextView) findViewById(R.id.tv_charge_thousand);
			tvThousnd.setVisibility(View.GONE);
		}else{
			tvTitle.setText("支付宝");
		}
		editTextInput = (EditText) findViewById(R.id.et_zhifu_input);
		mUser = ApplicationEx.get().getUserManager().getUser();
		tvAccount = (TextView) findViewById(R.id.tv_account);
		tvMoney = (TextView) findViewById(R.id.tv_account_money);
		if(mUser!=null){
			tvAccount.setText(mUser.getNickName());
			tvMoney.setText(String.valueOf(mUser.getUserCoin()));
		}
		for (int i = 0; i < tvIds.length; i++) {
			tv[i]=(TextView) findViewById(tvIds[i]);
		}
	}

	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.zhifu_bao_bt:
			if (mUser != null) {
				String input = editTextInput.getText().toString().trim();
				if (input.length() > 0) {
					if(IsFromMobile){
						getOrderIdFromMobilePay(input);
					}else{
						sendRequestToServer(input);
					}
				}else{
					Utils.MakeToast(getApplicationContext(), "请输入充值金额 ");
				}
				 
			} else {
				Utils.MakeToast(getApplicationContext(), "请先登录");
			}
			break;

		case R.id.tv_charge_thiry:
			editTextInput.setText("30");
			changeBg(R.id.tv_charge_thiry);
			break;
		case R.id.tv_charge_fifty:
			editTextInput.setText("50");
			changeBg(R.id.tv_charge_fifty);
			break;
		case R.id.tv_charge_hundred:
			editTextInput.setText("100");
			changeBg(R.id.tv_charge_hundred);
			break;
		case R.id.tv_charge_thiry_hundred:
			editTextInput.setText("300");
			changeBg(R.id.tv_charge_thiry_hundred);
			break;
		case R.id.tv_charge_five_hundred:
			editTextInput.setText("500");
			changeBg(R.id.tv_charge_five_hundred);
			break;
		case R.id.tv_charge_thousand:
			editTextInput.setText("1000");
			changeBg(R.id.tv_charge_thousand);
			break;
		}
	}

	
	public void changeBg(int id){
		for (int i = 0; i < tvIds.length; i++) {
			if(tvIds[i]==id){
				tv[i].setBackgroundResource(R.drawable.charge_num_bg_selected);
			}else{
				tv[i].setBackgroundResource(R.drawable.charge_num_bg);
			}
		}
	}
	private void getOrderIdFromMobilePay(String input) {
		ChargeMoney=Integer.valueOf(input);
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("amount",input);
		asyncHttpClient.post(AppConstants.GET_MOBILE_PAYMENT, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(ZhifuBaoActivity.this, "请稍后...");
				mDialog.show();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success = jsonObject.optBoolean("success");
						if (success) {
							JSONObject jsonObject2 = jsonObject.optJSONObject("retval");
							order_sn = jsonObject2.optString("tn"); // 后台生成的订单号
							doMobilePay();
						} else {
							String msg = jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(),msg);
						}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mDialog.dismiss();
				Utils.MakeToast(getApplicationContext(), "服务器连接超时");
			}
		});
	}

	/**
	 * 首先向后台发送 生成订单信息
	 * 
	 * @param input
	 */
	private void sendRequestToServer(String input) {
		ChargeMoney=Integer.valueOf(input);
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("amount", input);
		asyncHttpClient.post(AppConstants.GET_ZHI_FU_ORDER, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						mDialog=Utils.showProgressDialog(ZhifuBaoActivity.this,"请稍后...");
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
								boolean success = jsonObject
										.optBoolean("success");
								if (success) {
									JSONObject jsonObject2 = jsonObject.optJSONObject("retval");
									orderSubject = jsonObject2.optString("subject"); // 商品描述
									order_sn = jsonObject2.optString("order_sn"); // 后台生成的订单号
									amount = jsonObject2.optString("amount");// 商品价格
									notify_url = jsonObject2.optString("notify");// 回调地址
									doPayTask();
								} else {
									String msg = jsonObject.optString("msg");
									Utils.MakeToast(getApplicationContext(),msg);
								}

							} catch (JSONException e) {
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
	
	
	 /*****************************************************************
     * mMode参数解释：
     *      "00" - 启动银联正式环境
     *      "01" - 连接银联测试环境
     *****************************************************************/
	
	/**
	 * 调用手机支付问题
	 */
	protected void doMobilePay() {
		 int ret = UPPayAssistEx.startPay(ZhifuBaoActivity.this, null, null, order_sn, mMode);
         if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("提示");
             builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
             builder.setNegativeButton("确定",new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog,
                                 int which) {
                             dialog.dismiss();
                             UPPayAssistEx.installUPPayPlugin(ZhifuBaoActivity.this);
                         }
                     });

             builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {

                         @Override
                         public void onClick(DialogInterface dialog,int which) {
                             dialog.dismiss();
                         }
                     });
             builder.create().show();

         }
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        /************************************************* 
	         * 
	         *  步骤3：处理银联手机支付控件返回的支付结果 
	         *  
	         ************************************************/
	        if (data == null) {
	            return;
	        }

	        String msg = "";
	        /*
	         * 支付控件返回字符串:success、fail、cancel
	         *      分别代表支付成功，支付失败，支付取消
	         */
	        String str = data.getExtras().getString("pay_result");
	        if (str.equalsIgnoreCase("success")) {
	            msg = "支付成功！";
	            if(mFromTask){
					if(taskId2!=null){
						if(ChargeMoney>=500){
							Utils.doUserTask(mUser, taskId,null);
							Utils.doUserTask(mUser, taskId2, null);
						}else{
							Utils.doUserTask(mUser, taskId2, null);
						}
					}else{
						Utils.doUserTask(mUser, taskId,null);
					}
			}
	        } else if (str.equalsIgnoreCase("fail")) {
	            msg = "支付失败！";
	        } else if (str.equalsIgnoreCase("cancel")) {
	            msg = "用户取消了支付";
	        }
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("支付结果通知");
	        builder.setMessage(msg);
	        builder.setInverseBackgroundForced(true);
	        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.create().show();
	    }

	/*
	 * 调用支付宝支付
	 */
	private void doPayTask() {
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(
				ZhifuBaoActivity.this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper.showDialog(ZhifuBaoActivity.this, "提示",
					"缺少partner或者seller，请在PartnerConfig.java中增加.",
					R.drawable.infoicon);
			return;
		}
		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			// prepare the order info.
			// 准备订单信息
			String orderInfo = getOrderInfo();
//			Log.e("orderInfo", orderInfo);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
//			Log.e("orderInfo:", info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY,
					ZhifuBaoActivity.this);
			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(ZhifuBaoActivity.this,
						null, "正在支付", false, true);
			}
		} catch (Exception ex) {
			Toast.makeText(ZhifuBaoActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}

	}

	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
//				Log.e(TAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();
					BaseHelper.log(TAG, strRet);
					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									ZhifuBaoActivity.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if (tradeStatus.equals("9000")) {// 判断交易状态码，只有9000表示交易成功
								BaseHelper.showDialog(ZhifuBaoActivity.this,
										"提示", "支付成功.",
										R.drawable.infoicon);
								// buyGoldCoin();
								 if(mFromTask){
										if(taskId2!=null){
											if(ChargeMoney>=500){
												Utils.doUserTask(mUser, taskId,null);
												Utils.doUserTask(mUser, taskId2, null);
											}else{
												Utils.doUserTask(mUser, taskId2, null);
											}
										}else{
											Utils.doUserTask(mUser, taskId,null);
										}
								}
							} else {
								BaseHelper.showDialog(ZhifuBaoActivity.this,"提示", "支付失败.",R.drawable.infoicon);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(ZhifuBaoActivity.this, "提示",
								strRet, R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
	String getOrderInfo() {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + order_sn + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" +orderSubject+ "\"";
		strOrderInfo += "&";
		// 这笔交易内容
		strOrderInfo += "body=" + "\"" + orderSubject + "\"";
		strOrderInfo += "&";
		// 这笔交易价钱
		strOrderInfo += "total_fee=" + "\"" + amount.replace("￥", "") + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ notify_url + "\"";
		return strOrderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	// String getOutTradeNo() {
	// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
	// Date date = new Date();
	// String strKey = format.format(date);
	// java.util.Random r = new java.util.Random();
	// strKey = strKey + r.nextInt();
	// strKey = strKey.substring(0, 15);
	// return strKey;
	// }

	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(mFromTask){
			Utils.UpdateUserInfo(mUser, ZhifuBaoActivity.this);
//		}
	}
}
