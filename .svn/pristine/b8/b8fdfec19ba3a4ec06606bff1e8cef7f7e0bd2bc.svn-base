package com.ninexiu.sixninexiu.fragment;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.beans.VipBean;
import com.ninexiu.beans.VipBean.Vip;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.OnlyLoginActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class ShopVipFragment extends Fragment implements OnClickListener{
	private PullListView vipListView;
	private Dialog mDialog;
	private LinearLayout vipCategory;
	private TextView[] tvCategory;
	private TextView[] priceTextViews ;
	private TextView one_text,three_text,nine_text,twelve_text;
	private ImageView one_img,three_img,nine_img,tweleve_img;
	private VipBean bean;
	private User mUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_vip_layout, null);
		vipListView=(PullListView) view.findViewById(R.id.lv_vip);
		vipCategory=(LinearLayout) view.findViewById(R.id.vip_top);
		mUser = ApplicationEx.get().getUserManager().getUser();
		initView(view);
		parseData();
		return view;
	}
	
	private void initView(View view){
		one_text = (TextView) view.findViewById(R.id.vip_one_text);
		three_text = (TextView) view.findViewById(R.id.vip_three_text);
		nine_text = (TextView) view.findViewById(R.id.vip_nine_text);
		twelve_text = (TextView) view.findViewById(R.id.vip_twelve_text);
		priceTextViews = new TextView[]{one_text,three_text,nine_text,twelve_text};
		one_img = (ImageView) view.findViewById(R.id.vip_one_img);
		three_img = (ImageView) view.findViewById(R.id.vip_three_img);
		nine_img = (ImageView) view.findViewById(R.id.vip_nine_img);
		tweleve_img = (ImageView) view.findViewById(R.id.vip_twelve_img);
		one_img.setOnClickListener(this);
		three_img.setOnClickListener(this);
		nine_img.setOnClickListener(this);
		tweleve_img.setOnClickListener(this);
	}
	
	public void parseData(){
		InputStream inputStream=getResources().openRawResource(R.raw.shop);
		String content=Utils.getLineString(inputStream);
		if(content!=null){
			try {
				JSONObject jsonObject=new JSONObject(content);
				boolean success=jsonObject.optBoolean("success");
				if(success==true){
					JSONObject jsonObject2=jsonObject.optJSONObject("retval");
					JSONArray array=jsonObject2.optJSONArray("vip");
					final ArrayList<VipBean> data=new ArrayList<VipBean>();
					for (int i = 0; i < array.length(); i++) {

						JSONObject jsonObject3=array.getJSONObject(i);
						VipBean bean=new VipBean();
						bean.setVipName(jsonObject3.optString("name"));
						bean.setVipType(jsonObject3.optString("id"));
						ArrayList<Vip> vips=new ArrayList<Vip>();
						Vip vip1=bean.new Vip();
						String vipOne=jsonObject3.optString("oneMonth");
						if(Utils.isNotEmptyString(vipOne)){
							vip1.setMonth("1");
							vip1.setPrice(vipOne);
							vips.add(vip1);
						}
						
						Vip vip11=bean.new Vip();
						String vipOne1=jsonObject3.optString("unitprice");
						if(Utils.isNotEmptyString(vipOne1)){
							vip11.setMonth("1");
							vip11.setPrice(vipOne1);
							vips.add(vip11);
						}
						
						Vip vip2=bean.new Vip();
						String vipTwo=jsonObject3.optString("threeMonth");
						if(Utils.isNotEmptyString(vipTwo)){
							vip2.setMonth("3");
							vip2.setPrice(vipTwo);
							vips.add(vip2);
						}
						
						Vip vip3=bean.new Vip();
						String vipThree=jsonObject3.optString("sixMonth");
						if(Utils.isNotEmptyString(vipThree)){
							vip3.setMonth("6");
							vip3.setPrice(vipThree);
							vips.add(vip3);
						}
						Vip vip4=bean.new Vip();
						String vipFour=jsonObject3.optString("twelveMonth");
						if(Utils.isNotEmptyString(vipFour)){
							vip4.setMonth("12");
							vip4.setPrice(vipFour);
							vips.add(vip4);
						}
						bean.setVipData(vips);
						data.add(bean);
					}
					tvCategory=new TextView[data.size()];
					for (  int i = 0; i < data.size(); i++) {
						final VipBean bean=data.get(i);
						LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						params.gravity=Gravity.CENTER;
						params.weight=1;
						TextView tv=(TextView) LayoutInflater.from(getActivity()).inflate(R.layout.shop_text_view_layout, null);
						if(i==0){
							tv.setBackgroundResource(R.drawable.vip_selected);
						}else{
							params.leftMargin=10;
							tv.setBackgroundResource(R.drawable.vips_unselected);
						}
						tv.setClickable(true);
						tv.setGravity(Gravity.CENTER);
						tv.setFocusable(true);
						tv.setText(bean.getVipName());
						tv.setLayoutParams(params);
						tv.setId(i);
						vipCategory.addView(tv);
						tvCategory[i]=tv;
						tv.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								for (int j = 0; j < tvCategory.length; j++) {
									if(v.getId()==tvCategory[j].getId()){
										tvCategory[j].setBackgroundResource(R.drawable.vip_selected);
									}else{
										tvCategory[j].setBackgroundResource(R.drawable.vips_unselected);
									}
								}
//								VipAdapter adapter=new VipAdapter(getActivity(), bean,false,null);
//								vipListView.setAdapter(adapter);
								ShopVipFragment.this.bean =bean;
								for (int i = 0; i < 4; i++) {
									priceTextViews[i].setText(bean.getVipData().get(i).getPrice());
								}
							}
						});
					}
					
					if(data.size()>0){
						VipBean bean=data.get(0);
						this.bean = bean;
//						VipAdapter adapter=new VipAdapter(getActivity(), bean,false,null);
//						vipListView.setAdapter(adapter);
						for (int i = 0; i < 4; i++) {
							priceTextViews[i].setText(bean.getVipData().get(i).getPrice());
						}
					}
					
				}else{
					String msg=jsonObject.optString("msg");
					Utils.MakeToast(getActivity(), msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vip_one_img:
//			buyVipTask(bean, 1+"");
			showChargeSureDialog(getActivity(), 1+"");
			break;
			
		case R.id.vip_three_img:
//			buyVipTask(bean, 3+"");
			showChargeSureDialog(getActivity(), 3+"");
			break;
		case R.id.vip_nine_img:
//			buyVipTask(bean, 9+"");
			showChargeSureDialog(getActivity(), 9+"");
			break;
		case R.id.vip_twelve_img:
//			buyVipTask(bean, 12+"");
			showChargeSureDialog(getActivity(), 12+"");
			break;
			

		}
	
		// TODO Auto-generated method stub
		
	}
	private  void showChargeSureDialog(final Context mContext,final String str){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		builder.setTitle("购买");
		builder.setMessage("是否购买该道具？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mUser!=null) {
					buyVipTask(bean, str);
				}
				else {
					Intent intent= new Intent(getActivity().getApplicationContext(),OnlyLoginActivity.class); 
					startActivity(intent);
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	private void buyVipTask(VipBean vipBean,String month) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		String url=null;

			url=AppConstants.USER_BUY_VIP;
			params.put("viptype", vipBean.getVipType());
		
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("buytime", month);
		asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(getActivity(), "购买中...");
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
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							Utils.MakeToast(getActivity(), "购买成功");
							Utils.UpdateUserInfo(mUser, getActivity());
						}else{
							String msg=jsonObject.optString("msg");
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
