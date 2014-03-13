package com.ninexiu.sixninexiu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.Utils;

/**
 * 手机充值卡充值
 * @author mao
 *
 */
public class MobileCardCharge extends BaseActivity {
	private User mUser;
	private TextView tvAccount,tvMoney;
	private int[] ids=new int[]{R.id.iv_mobile_tag,R.id.iv_union_tag,R.id.iv_dianxin_tag};
	private ImageView[] views=new ImageView[ids.length];
	private int[] tvIds=new int[]{R.id.tv_thithy,R.id.tv_fifty,R.id.tv_hundred};
	private TextView[] tvCardMoney=new TextView[tvIds.length];
	private int tvChargeMoney=30; //选择的充值卡的金额
	private String cardType="SZX"; //卡类型
	private boolean mFromTask;
	private String taskId;
	private String taskId2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_card_charge_layout);
		tvTitle.setText("手机充值卡");
		mUser=ApplicationEx.get().getUserManager().getUser();
		tvAccount=(TextView) findViewById(R.id.tv_account);
		tvMoney=(TextView) findViewById(R.id.tv_account_money);
		if(mUser!=null){
			tvAccount.setText(mUser.getNickName());
			tvMoney.setText(String.valueOf(mUser.getUserCoin()));
		}
		for (int i = 0; i < ids.length; i++) {
			views[i]=(ImageView) findViewById(ids[i]);
		}
		
		for (int i = 0; i < tvIds.length; i++) {
			tvCardMoney[i]=(TextView) findViewById(tvIds[i]);
		}
		
		Intent intent=getIntent();
		mFromTask=intent.getBooleanExtra("fromtask", false);
		taskId=intent.getStringExtra("taskid");
		taskId2=intent.getStringExtra("taskid2");
	}
	
	public void onClick(View view){
		super.onClick(view);
		switch (view.getId()) {
		case R.id.layout_china_mobile:
			changeBg(0);
			cardType="SZX";
			break;

		case R.id.layout_china_union:
			changeBg(1);
			cardType="UNICOM";
			break;
			
		case R.id.layout_china_dianxin:
			changeBg(2);
			cardType="TELECOM";
			break;
			
		case R.id.tv_thithy:
			changeTvBg(R.id.tv_thithy);
			tvChargeMoney=30;
			break;
			
		case R.id.tv_fifty:
			changeTvBg(R.id.tv_fifty);
			tvChargeMoney=50;
			break;
			
		case R.id.tv_hundred:
			changeTvBg(R.id.tv_hundred);
			tvChargeMoney=100;
			break;
			
		case R.id.bt_mobile_card_charge:
			if(mUser!=null){
				if(tvChargeMoney==0){
					Utils.MakeToast(getApplicationContext(), "请选择充值金额");
				}else{
					Intent intent=new Intent(this,MobileCardInputActivity.class);
					intent.putExtra("cardType", cardType);
					intent.putExtra("chargeMoney", tvChargeMoney);
					intent.putExtra("user", mUser);
					if(mFromTask){
						intent.putExtra("fromtask", true);
						intent.putExtra("taskid", taskId);
						intent.putExtra("taskid2", taskId2);
					}
					startActivity(intent);
				}
			}else{
				Utils.MakeToast(this, "请先登录");
			}
			break;
		}
	}
	
	

	private void changeTvBg(int tvId) {
		for (int i = 0; i < tvIds.length; i++) {
			if(tvId==tvIds[i]){
				tvCardMoney[i].setBackgroundResource(R.drawable.mobile_card_num_selected);
			}else{
				tvCardMoney[i].setBackgroundResource(R.drawable.mobile_card_num_unselected);
			}
		}
		
		
	}

	public void changeBg(int id){
		for (int i = 0; i < ids.length; i++) {
			if(id==i){
				views[i].setVisibility(View.VISIBLE);
			}else{
				views[i].setVisibility(View.GONE);
			}
		}
	}

}
