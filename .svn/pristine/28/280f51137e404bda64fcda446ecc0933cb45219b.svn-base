package com.ninexiu.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Father;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

public class FansAdapter extends BaseAdapter {
	
	private AsyncImageLoader asyncImageLoader;
	private Context context;
	private ArrayList<Father> datas;
	private Bitmap bitmap;
	
	public FansAdapter(Activity context,ArrayList<Father> data) {
		this.context=context;
		this.datas=data;
		//头像变圆		
		asyncImageLoader = new AsyncImageLoader();
		bitmap = Utils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
//		Log.e("test", "bitmap=="+bitmap.toString());
	}
	
	@Override
	public int getCount() {
		if(datas!=null){
			return datas.size();
		}else{
			return 0;
		}
		
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
		final FanViewHolder viewHolder;
		Father father=datas.get(position);
		if(convertView==null){
			viewHolder=new FanViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.room_fans_list_item, null);
			viewHolder.rankhead = (TextView) convertView.findViewById(R.id.rank_head);
			viewHolder.ivHead=(ImageView) convertView.findViewById(R.id.iv_fans_head);
//			viewHolder.tvRank=(TextView) convertView.findViewById(R.id.tv_fans_rank);
			viewHolder.tvNickName=(TextView) convertView.findViewById(R.id.tv_fans_name);
			viewHolder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_fans_level);
			viewHolder.tvPay=(TextView) convertView.findViewById(R.id.tv_fan_money);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(FanViewHolder) convertView.getTag();
		}
		
		//头像变圆		
		//asyncImageLoader = new AsyncImageLoader();
//		Log.e("test", "father.getAvatar=="+father.getAvatar()+"a");
		if (position<=2) {
			viewHolder.rankhead.setVisibility(0);
			viewHolder.rankhead.setText(position+1+"");
			viewHolder.rankhead.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rank_head_red));
		}
		if (position==3||position==4||position==5) {
			viewHolder.rankhead.setVisibility(0);
			viewHolder.rankhead.setText(position+1+"");
			viewHolder.rankhead.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rank_head_black));
		}
		if(position>5){
			viewHolder.rankhead.setVisibility(8);
		}
		
		if (father.getAvatar().length()>1){
			Bitmap map=asyncImageLoader.loadBitmap(father.getAvatar(), new AsyncImageLoader.ImageCallback() {
				@Override
				public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
					viewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
				}
			});
			if(map==null){
				viewHolder.ivHead.setImageBitmap(bitmap);
			}else{
				viewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
			}
		}else {
			viewHolder.ivHead.setImageBitmap(bitmap);
		}
//		ImageLoader.getInstance().displayImage(father.getAvatar(), viewHolder.ivHead,false);

//		bitmapLoad.display(viewHolder.ivHead, father.getAvatar());
		
//		viewHolder.tvRank.setVisibility(View.VISIBLE);
//		if(position<=2){
//			viewHolder.tvRank.setText(position+1+"");
//		}else{
//			viewHolder.tvRank.setText("");
//			viewHolder.tvRank.setVisibility(View.GONE);
//		}
		Utils.setUserLevel(father.getWeath(), viewHolder.ivLevel);
		viewHolder.tvNickName.setText(father.getNickName());
		viewHolder.tvPay.setText(father.getTotal());
		return convertView;
	}
	
	class FanViewHolder {
		public ImageView ivHead;
		public TextView tvNickName;
		public ImageView ivLevel;
		public TextView tvPay;
		public TextView rankhead;
	}

}
