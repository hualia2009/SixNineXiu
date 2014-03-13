package com.ninexiu.sixninexiu;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ninexiu.adapter.HostAdapter;
import com.ninexiu.adapter.VipAdapter;
import com.ninexiu.beans.User;
import com.ninexiu.beans.VipBean;
import com.ninexiu.beans.VipBean.Vip;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;
import com.umeng.analytics.MobclickAgent;
/*
 * 贵族界面
 */
public class NobelActivity extends Activity implements OnClickListener{
	private User mUser;
	private PullListView listView;
	private Dialog mDialog;
	private LinearLayout nobelCategory;
	private Button choiceHost;
	private ArrayList<String> rid=new ArrayList<String>();
	private ArrayList<String> hostName=new ArrayList<String>();
	private String selectRoomId;//选择守护的房间ID
	private ImageView ivMen,ivCar;
	private int[] ids=new int[]{R.drawable.shop_nobel_men,R.drawable.shop_nobel_qi,R.drawable.shop_nobel_fa,
			R.drawable.shop_nobel_shi,R.drawable.shop_nobel_jiao};
	private int[] cars=new int[]{R.drawable.shop_men_car,R.drawable.shop_qi_car,R.drawable.shop_fa_car,
			R.drawable.shop_shi_car,R.drawable.shop_jiao_car};
	private VipAdapter VipAdapter;
	private TextView[] tvCategory;
	private int width,height;
	private TextView[] priceTextViews ;
	private TextView one_text,three_text,nine_text,twelve_text;
	private ImageView one_img,three_img,nine_img,tweleve_img;
	private VipBean vipBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_nobel_layout);
		mUser=ApplicationEx.get().getUserManager().getUser();
		listView=(PullListView) findViewById(R.id.lv_noble);
		nobelCategory=(LinearLayout) findViewById(R.id.layout_nobel_category);
		choiceHost=(Button)findViewById(R.id.spinner_choice_host);
		ivMen=(ImageView) findViewById(R.id.iv_icon_men);
		ivCar=(ImageView) findViewById(R.id.iv_car);
		
		one_text = (TextView) findViewById(R.id.shop_one_text);
		three_text = (TextView) findViewById(R.id.shop_three_text);
		nine_text = (TextView) findViewById(R.id.shop_nine_text);
		twelve_text = (TextView) findViewById(R.id.shop_twelve_text);
		priceTextViews = new TextView[]{one_text,three_text,nine_text,twelve_text};
		one_img = (ImageView) findViewById(R.id.shop_one_img);
		three_img = (ImageView) findViewById(R.id.shop_three_img);
		nine_img = (ImageView) findViewById(R.id.shop_nine_img);
		tweleve_img = (ImageView) findViewById(R.id.shop_twelve_img);
		one_img.setOnClickListener(this);
		three_img.setOnClickListener(this);
		nine_img.setOnClickListener(this);
		tweleve_img.setOnClickListener(this);
		
		
		width=(int) getResources().getDimension(R.dimen.nobel_pop_host_width);
		height=(int) getResources().getDimension(R.dimen.nobel_pop_host_height);
		doGetAllHostList();
//		doBuyNobelTask();
		parserNoble();
		
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
	
	/*
	 * 得到所有主播的列表
	 */
	private void doGetAllHostList() {
		
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		asyncHttpClient.post(AppConstants.GET_ALL_HOST, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							JSONArray array=jsonObject.optJSONArray("retval");
							rid.clear();
							hostName.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2=array.getJSONObject(i);
								rid.add(jsonObject2.optString("rid"));
								hostName.add(jsonObject2.optString("nickname"));
							}
							choiceHost.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									View contentView=getLayoutInflater().inflate(R.layout.nobel_pop_host_layout, null);
									final PopupWindow popupWindow=new PopupWindow(contentView, width, height);
									popupWindow.setOutsideTouchable(true);
									popupWindow.setTouchable(true);
									popupWindow.setFocusable(true);
									popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
									popupWindow.showAsDropDown(choiceHost);
									PullListView listView=(PullListView) contentView.findViewById(R.id.listView1);
									HostAdapter adapter = new HostAdapter(NobelActivity.this, hostName);
									listView.setAdapter(adapter);
									listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											selectRoomId=rid.get(position);
											choiceHost.setText(hostName.get(position));
											popupWindow.dismiss();
											if(VipAdapter!=null){
												VipAdapter.setSelectRoomId(selectRoomId);
											}
										}
									});
								}
							});
							/**
							 * 默认选中第一项
							 */
//							selectRoomId=rid.get(0);
//							if(VipAdapter!=null){
//								VipAdapter.setSelectRoomId(selectRoomId);
//							}
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
				Utils.MakeToast(getApplicationContext(), "获取主播列表失败");
			}
		});
		
	}
	public void doBuyNobelTask(){
	AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
	asyncHttpClient.post(AppConstants.SHOP_INTERFACE, new AsyncHttpResponseHandler(){
		@Override
		public void onStart() {
			super.onStart();
			mDialog=Utils.showProgressDialog(NobelActivity.this, "获取数据中...");
			mDialog.show();
		}
		
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			mDialog.dismiss();
		}
		
		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			mDialog.dismiss();
		}
		
	});
}
	
	public void parserNoble(){
		InputStream inputStream=getResources().openRawResource(R.raw.shop);
		String content=Utils.getLineString(inputStream);
		if(content!=null){
			try {
				JSONObject jsonObject=new JSONObject(content);
				boolean success=jsonObject.optBoolean("success");
				if(success==true){
					JSONObject jsonObject2=jsonObject.optJSONObject("retval");
					JSONArray array=jsonObject2.optJSONArray("aristocrat");
					final ArrayList<VipBean> data=new ArrayList<VipBean>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject3=array.getJSONObject(i);
						VipBean bean=new VipBean();
						bean.setVipName(jsonObject3.optString("name"));
						bean.setVipType(jsonObject3.optString("id"));
						bean.setAristocratName(jsonObject3.optString("aristocrat_name"));
						ArrayList<Vip> vips=new ArrayList<Vip>();
						Vip vip1=bean.new Vip();
						String vipOne=jsonObject3.optString("oneMonth");
						if(Utils.isNotEmptyString(vipOne)){
							vip1.setMonth("1");
							vip1.setPrice(vipOne);
							vips.add(vip1);
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
						//Log.e("test","sixMonth="+vipThree);
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
					for (int i = 0; i < data.size(); i++) {
						final VipBean bean=data.get(i);
						LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						params.gravity=Gravity.CENTER;
						params.weight=1;
						TextView tv=(TextView) LayoutInflater.from(NobelActivity.this).inflate(R.layout.shop_text_view_layout, null);
						if(i==0){
							tv.setBackgroundResource(R.drawable.vip_selected);
						}else{
							params.leftMargin=10;
							tv.setBackgroundResource(R.drawable.vips_unselected);
						}
						tv.setText(bean.getVipName());
						tv.setLayoutParams(params);
						tv.setTag(i);
						tv.setGravity(Gravity.CENTER);
						tv.setId(i);
						tv.setClickable(true);
						tv.setFocusable(true);
						nobelCategory.addView(tv);
						tvCategory[i]=tv;
						tv.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ivMen.setImageResource(ids[(Integer) v.getTag()]);
								ivCar.setImageResource(cars[(Integer) v.getTag()]);
								for (int j = 0; j < tvCategory.length; j++) {
									if(v.getId()==tvCategory[j].getId()){
										tvCategory[j].setBackgroundResource(R.drawable.vip_selected);
									}else{
										tvCategory[j].setBackgroundResource(R.drawable.vips_unselected);
									}
								}
//								VipAdapter=new VipAdapter(NobelActivity.this,bean,true,selectRoomId);
//								listView.setAdapter(VipAdapter);
								NobelActivity.this.vipBean = bean;
								for (int i = 0; i < 4; i++) {
									priceTextViews[i].setText(bean.getVipData().get(i).getPrice());
								}
								
								
							}
						});
					}
					
					if(data.size()>0){
						VipBean bean=data.get(0);
						NobelActivity.this.vipBean = bean;
//							VipAdapter=new VipAdapter(NobelActivity.this,bean,true,selectRoomId);
//							listView.setAdapter(VipAdapter);
						for (int i = 0; i < 4; i++) {
							priceTextViews[i].setText(bean.getVipData().get(i).getPrice());
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shop_one_img:
//			buyVipTask(vipBean, 1+"");
			showChargeSureDialog(this, 1+"");
			break;
		
		case R.id.shop_three_img:
//			buyVipTask(vipBean, 3+"");
			showChargeSureDialog(this, 3+"");
			break;
		case R.id.shop_nine_img:
//			buyVipTask(vipBean, 9+"");
			showChargeSureDialog(this, 9+"");
			break;
		case R.id.shop_twelve_img:
//			buyVipTask(vipBean, 12+"");
			showChargeSureDialog(this, 12+"");
			break;
		
		}
	}
	
	private  void showChargeSureDialog(final Context mContext,final String str){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		builder.setTitle("购买");
		builder.setMessage("是否购买该道具？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				buyVipTask(vipBean, str);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	private void buyVipTask(VipBean vipBean,String month) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		String url=null;
//		if(isNobel){
			if(selectRoomId!=null){
				url=AppConstants.BUY_NOBEL;
				params.put("rid", selectRoomId);
				params.put("aristocrat_name",vipBean.getAristocratName());
			}else{
				Utils.MakeToast(NobelActivity.this, "请选择守护主播");
				return ;
			}
			
//		}
//		else{
//			url=AppConstants.USER_BUY_VIP;
//			params.put("viptype", vipType);
//		}
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("buytime", month);
		asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(NobelActivity.this, "购买中...");
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
							Utils.MakeToast(NobelActivity.this, "购买成功");
							Utils.UpdateUserInfo(mUser, NobelActivity.this);
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(NobelActivity.this, msg);
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
				Utils.MakeToast(NobelActivity.this, "网络连接超时");
				
			}
		});
		
	}

}
