package com.ninexiu.sixninexiu.fragment;

import com.ninexiu.sixninexiu.NobelActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.VipActivity;
import com.ninexiu.sixninexiu.R.drawable;
import com.ninexiu.sixninexiu.R.id;
import com.ninexiu.sixninexiu.R.layout;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * 商店页面
 * 
 * @author Administrator
 * 
 */
public class ShopFragment extends BaseFragment implements OnClickListener{

	private TextView vipTextView,nobelTextView;
	private TextView title;
//    android.support.v4.app.Fragment shopVipFragment;
//	private android.support.v4.app.Fragment nobelFragment;
    private View vip,nobel;




	@Override
	public LayoutInflater getLayoutInflater() {
		// TODO Auto-generated method stub
		return super.getLayoutInflater();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_layout, null);
//		shopVipFragment = getFragmentManager().findFragmentById(R.id.tab1);
//		nobelFragment = getFragmentManager().findFragmentById(R.id.tab2);
		title=(TextView) view.findViewById(R.id.title_text);
		title.setText("商城");
		vipTextView = (TextView) view.findViewById(R.id.vip_text);
		nobelTextView = (TextView) view.findViewById(R.id.nobel_text);
		vip = view.findViewById(R.id.shop_vip);
		nobel = view.findViewById(R.id.shop_nobel);
		vipTextView.setOnClickListener(this);
		nobelTextView.setOnClickListener(this);
		return view;
	}

	public void onClick(View view){
		switch (view.getId()) {
		case R.id.left_bt:
			getActivity().finish();
			break;

		case R.id.vip_text:
			vipTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shop_select));
			nobelTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shop_nobel_unselect));
			vip.setVisibility(0);
			nobel.setVisibility(8);
			break;
			
		case R.id.nobel_text:
			nobelTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shop_nobel_select));
			vipTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shop_unselect));
			vip.setVisibility(8);
			nobel.setVisibility(0);
			break;
		}
	}
	
}
