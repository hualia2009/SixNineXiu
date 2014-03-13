package com.ninexiu.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.beans.VipBean;
import com.ninexiu.beans.VipBean.Vip;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;

/**
 * VIP 适配器
 * @author mao
 *
 */
public class VipAdapter extends BaseAdapter {

	private Activity mContext;
	private ArrayList<Vip> data;
	private User mUser;
	private String vipType;
	private String nobelName;
	private Dialog mDialog;
	private String selectRid;
	private boolean isNobel;//用来区分是购买VIP 还是贵族
	
	public VipAdapter(Activity context,VipBean bean, boolean mIsNoble, String selectRoomId) {
		this.mContext=context;
		this.data=bean.getVipData();
		mUser=ApplicationEx.get().getUserManager().getUser();
		this.vipType=bean.getVipType();
		nobelName=bean.getAristocratName();
		this.isNobel=mIsNoble;
		this.selectRid=selectRoomId;
	}
	
	public void setSelectRoomId(String id){
		this.selectRid=id;
		//Log.e("selectRomid", id);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VipHolder holder=null;
		final Vip vip=data.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.shop_vip_list_item_layout, null);
			holder=new VipHolder();
			holder.tvPrice=(TextView) convertView.findViewById(R.id.tv_vip_price);
			holder.tvVip=(TextView) convertView.findViewById(R.id.tv_month);
			holder.tvBuy=(TextView) convertView.findViewById(R.id.tv_buy);
			holder.tvDiscount=(TextView) convertView.findViewById(R.id.tv_discount);
			convertView.setTag(holder);
		}else{
			holder=(VipHolder) convertView.getTag();
		}
		if(isNobel){
			holder.tvDiscount.setVisibility(View.GONE);
		}else{
			if(position==0){
				holder.tvDiscount.setVisibility(View.GONE);
			}else if(position==1){
				holder.tvDiscount.setVisibility(View.VISIBLE);
				holder.tvDiscount.setText("9折");
			} else if(position==2){
				holder.tvDiscount.setVisibility(View.VISIBLE);
				holder.tvDiscount.setText("8.5折");
			}else if(position==3){
				holder.tvDiscount.setVisibility(View.VISIBLE);
				holder.tvDiscount.setText("8折");
			}
		}
		holder.tvVip.setText(vip.getMonth()+"个月");
		holder.tvPrice.setText(vip.getPrice()+"九币");
		holder.tvBuy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mUser!=null){
					if(Long.valueOf(mUser.getUserCoin())>=Long.valueOf(vip.getPrice())){
						buyVipTask(vip);
					}else{
						Utils.showChargeDialog(mContext);
					}
					
				}else{
//					Utils.MakeToast(mContext, "请先登录");
					Utils.showLoginDialog(mContext,false);
				}
			}
		});
		
		return convertView;
	}
	
	protected void buyVipTask(Vip vip) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		String url=null;
		if(isNobel){
			if(selectRid!=null){
				url=AppConstants.BUY_NOBEL;
				params.put("rid", selectRid);
				params.put("aristocrat_name",nobelName);
			}else{
				Utils.MakeToast(mContext, "请选择守护主播");
				return ;
			}
			
		}else{
			url=AppConstants.USER_BUY_VIP;
			params.put("viptype", vipType);
		}
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("buytime", vip.getMonth());
		asyncHttpClient.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(mContext, "购买中...");
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
							Utils.MakeToast(mContext, "购买成功");
							Utils.UpdateUserInfo(mUser, mContext);
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(mContext, msg);
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
				Utils.MakeToast(mContext, "网络连接超时");
				
			}
		});
		
	}

	class VipHolder{
		public TextView tvVip;
		public TextView tvPrice;
		public TextView tvBuy;
		public TextView tvDiscount;
	}

}
