package com.ninexiu.sixninexiu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.MobileCardCharge;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.ZhifuBaoActivity;
import com.ninexiu.utils.Utils;

/**
 * 充值界面
 * 
 * @author mao
 * 
 */
public class ChargeFragment extends BaseFragment {
	private User mUser;
	private boolean mFromTask = false;
	private String mTaskId;
	private String mTaskId2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.charge_layout, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews(getView());
	}

	private void initViews(View view) {
		mUser = ApplicationEx.get().getUserManager().getUser();
		Intent intent = getActivity().getIntent();
		mFromTask = intent.getBooleanExtra("fromtask", false);
		mTaskId = intent.getStringExtra("taskid");
		mTaskId2 = intent.getStringExtra("taskid2");
		view.findViewById(R.id.zhi_fu_bao).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.mobile_charge).setOnClickListener(mOnClickListener);
		view.findViewById(R.id.mobile_card_charge).setOnClickListener(mOnClickListener);
	}

	@Override
	public int getTopTitle() {
		return R.string.charge;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.zhi_fu_bao: // 支付宝充值
				if (mUser != null) {
					Intent intentZhi = new Intent(getActivity(), ZhifuBaoActivity.class);
					if (mFromTask) {
						intentZhi.putExtra("fromtask", true);
						intentZhi.putExtra("taskid", mTaskId);
						intentZhi.putExtra("taskid2", mTaskId2);
					}
					intentZhi.putExtra("mobile", false);
					startActivity(intentZhi);
				} else {
					Utils.showLoginDialog(getActivity(), false);
				}
				break;

			case R.id.mobile_charge:// 手机充值
				if (mUser != null) {
					Intent intentChong = new Intent(getActivity(), ZhifuBaoActivity.class);
					if (mFromTask) {
						intentChong.putExtra("fromtask", true);
						intentChong.putExtra("taskid", mTaskId);
						intentChong.putExtra("taskid2", mTaskId2);
					}
					intentChong.putExtra("mobile", true);
					startActivity(intentChong);
				} else {
					Utils.showLoginDialog(getActivity(), false);
				}
				break;

			case R.id.mobile_card_charge:// 手机卡充值
				if (mUser != null) {
					Intent intentCard = new Intent(getActivity(), MobileCardCharge.class);
					if (mFromTask) {
						intentCard.putExtra("fromtask", true);
						intentCard.putExtra("taskid", mTaskId);
						intentCard.putExtra("taskid2", mTaskId2);
					}
					startActivity(intentCard);
				} else {
					Utils.showLoginDialog(getActivity(), false);
				}
				break;
			}
		}
	};

}
