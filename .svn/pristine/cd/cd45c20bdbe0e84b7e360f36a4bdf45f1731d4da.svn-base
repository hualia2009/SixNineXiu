package com.ninexiu.sixninexiu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.Utils;

/**
 * 充值界面
 * @author mao
 *
 */
public class ChargeActivity extends BaseActivity{
	private User mUser;
	private boolean mFromTask=false;
	private String mTaskId;
	private String mTaskId2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charge_layout);
		tvTitle.setText("充值");
		mUser=ApplicationEx.get().getUserManager().getUser();
		Intent intent=getIntent();
		mFromTask=intent.getBooleanExtra("fromtask", false);
		mTaskId=intent.getStringExtra("taskid");
		mTaskId2=intent.getStringExtra("taskid2");
	}
	
	public void onClick(View view){
		super.onClick(view);
		switch (view.getId()) {
		case R.id.zhi_fu_bao: //支付宝充值
			if(mUser!=null){
				Intent intentZhi=new Intent(this,ZhifuBaoActivity.class);
				if(mFromTask){
					intentZhi.putExtra("fromtask", true);
					intentZhi.putExtra("taskid", mTaskId);
					intentZhi.putExtra("taskid2", mTaskId2);
				}
				intentZhi.putExtra("mobile", false);
				startActivity(intentZhi);
			}else{
				Utils.showLoginDialog(this,false);
			}
			break;

		case R.id.mobile_charge://手机充值
			if(mUser!=null){
				Intent intentChong=new Intent(this, ZhifuBaoActivity.class);
				if(mFromTask){
					intentChong.putExtra("fromtask", true);
					intentChong.putExtra("taskid", mTaskId);
					intentChong.putExtra("taskid2", mTaskId2);
				}
				intentChong.putExtra("mobile", true);
				startActivity(intentChong);
			}else{
				Utils.showLoginDialog(this,false);
			}
			break;
			
		case R.id.mobile_card_charge://手机卡充值
			if(mUser!=null){
				Intent intentCard=new Intent(this,MobileCardCharge.class);
				if(mFromTask){
					intentCard.putExtra("fromtask", true);
					intentCard.putExtra("taskid", mTaskId);
					intentCard.putExtra("taskid2", mTaskId2);
				}
				startActivity(intentCard);
			}else{
				Utils.showLoginDialog(this,false);
			}
			break;
		}
	}

}
