package com.ninexiu.sixninexiu;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Father;
import com.ninexiu.beans.Host;
import com.ninexiu.beans.User;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

/*
 * 得到所有已经关注过的主播列表  .返回数据格式暂无房间ID
 */
public class HostHasAttActivity extends BaseActivity {

	private PullListView lv;
	private User mUser;
	private ArrayList<Host> hostAtts=new ArrayList<Host>();
	private HostAttAdapter attAdapter;
//	private FinalBitmap bitmapLoad;
	private AsyncImageLoader asyncImageLoader;
	private Dialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host_att_layout);
		tvTitle.setText("我关注的主播");
		lv=(PullListView) findViewById(R.id.lv_host_att);
		View emptyView=getLayoutInflater().inflate(R.layout.layout_empty_view, null);
		TextView tvIndicator=(TextView) emptyView.findViewById(R.id.tv_empty);
		mUser=ApplicationEx.get().getUserManager().getUser();
//		bitmapLoad=Utils.getBitmapLoad(this,false);
		asyncImageLoader=new AsyncImageLoader();
		if(mUser!=null){
			getHostAtt();
			tvIndicator.setText("关注列表为空");
		}else{
			tvIndicator.setText("请先登录");
			Intent intent=new Intent(this,OnlyLoginActivity.class);
			startActivity(intent);
			finish();
		}
		lv.setEmptyView(emptyView);
	}
	
	private void getHostAtt() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_ALL_ATT_LIST, params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(HostHasAttActivity.this, "读取数据中...");
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
						JSONObject object=jsonObject.optJSONObject("retval");
						JSONArray array=object.optJSONArray("myfollowers");
						hostAtts.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2=array.getJSONObject(i);
							Host host=new Host();
							host.setUid(jsonObject2.optString("followuid")); //主播自己的ID
							host.setHostAttId(jsonObject2.optString("id"));
							host.setAvatar(jsonObject2.optString("avatar"));
							host.setNickName(jsonObject2.optString("nickname"));
							JSONObject jsonObject3=jsonObject2.optJSONObject("openstatic");
							host.setAudice(jsonObject3.optString("count"));
							host.setIsPlay(jsonObject3.optString("static"));
							host.setUserNum(jsonObject2.optString("usernum"));
							host.setWeath(jsonObject2.optString("wealth"));
							host.setCredit(jsonObject2.optString("credit"));
							host.setUserType(jsonObject2.optString("usertype"));
							host.setRoomTag(jsonObject2.optString("room_ext1"));
							host.setRoomId(jsonObject2.optString("rid"));
							hostAtts.add(host);
						}
						attAdapter=new HostAttAdapter();
						lv.setAdapter(attAdapter);
						lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent=new Intent(HostHasAttActivity.this,ChatRoomActivity.class);
								intent.putExtra("host", (Father)parent.getAdapter().getItem(position));
								startActivity(intent);
							}
						});
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
				Utils.MakeToast(getApplicationContext(), "连接超时");
			}
			
		});
	}
	
	
	class HostAttAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return hostAtts.size();
		}

		@Override
		public Father getItem(int position) {
			return hostAtts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HostAttHolder holder;
			final Host host=hostAtts.get(position);
			if(convertView==null){
				holder=new HostAttHolder();
				convertView=getLayoutInflater().inflate(R.layout.host_att_list_item_two, null);
				holder.ivHead=(ImageView) convertView.findViewById(R.id.iv_att_head);
				holder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_host_level);
				holder.tvName=(TextView) convertView.findViewById(R.id.tv_nickname);
//				holder.tvNum=(TextView) convertView.findViewById(R.id.tv_liang);
				holder.ivWealth = (ImageView)convertView.findViewById(R.id.iv_userlever);
				holder.btCancle=(ImageView) convertView.findViewById(R.id.iv_cancel);
				convertView.setTag(holder);
			}else{
				holder=(HostAttHolder) convertView.getTag();
			}
			Bitmap map=asyncImageLoader.loadBitmap(host.getAvatar(), new AsyncImageLoader.ImageCallback() {
				
				@Override
				public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
					holder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
				}
			});
			if(map==null){
				holder.ivHead.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.loading_image)));
			}else{
				holder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
			}
//			bitmapLoad.display(holder.ivHead, host.getAvatar());
			if (Utils.isNotEmptyString(host.getWeath())) {
				Utils.setUserLevel(host.getWeath(), holder.ivWealth);
			}
			holder.tvName.setText(host.getNickName());
//			holder.tvNum.setText("("+host.getUserNum()+")");
			Utils.setHostLevel(host.getCredit(), holder.ivLevel);
			holder.btCancle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					doUnAttTask(host);
				}
			});
			return convertView;
		}
		
		
		protected void doUnAttTask(final Host host) {
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			params.put("cancleId", host.getHostAttId());
			asyncHttpClient.post(AppConstants.PAY_HOST_UNATTE,params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
					mDialog=Utils.showProgressDialog(HostHasAttActivity.this,"操作中……");
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
							boolean success=jsonObject.getBoolean("success");
							if(success==true){
								Utils.MakeToast(getApplicationContext(), "取消成功");
								hostAtts.remove(host);
								attAdapter.notifyDataSetChanged();
							}else{
								String message=jsonObject.optString("msg");
								Utils.MakeToast(getApplicationContext(), message);
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
			
		}


		public class HostAttHolder{
			public ImageView ivHead;
			public ImageView ivLevel;
			public ImageView ivWealth;
			public TextView tvName;
			public TextView tvNum;
			public ImageView btCancle;
		}
	}
}
