package com.ninexiu.sixninexiu;

import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.fragment.ShopNobelFragment;
import com.ninexiu.sixninexiu.fragment.ShopVipFragment;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
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
public class ShopActivity extends Activity implements OnClickListener{

	private TextView vipTextView,nobelTextView;
	private TextView title;
    private Fragment shopVipFragment,nobelFragment;
    private View vip,nobel;



	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_layout);
		shopVipFragment = getFragmentManager().findFragmentById(R.id.tab1);
		nobelFragment = getFragmentManager().findFragmentById(R.id.tab2);
		title=(TextView) findViewById(R.id.title_text);
		title.setText("商城");
		vipTextView = (TextView) findViewById(R.id.vip_text);
		nobelTextView = (TextView) findViewById(R.id.nobel_text);
		vip = findViewById(R.id.shop_vip);
		nobel = findViewById(R.id.shop_nobel);
		vipTextView.setOnClickListener(this);
		nobelTextView.setOnClickListener(this);
	}

	public void onClick(View view){
		switch (view.getId()) {
		case R.id.left_bt:
			finish();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
