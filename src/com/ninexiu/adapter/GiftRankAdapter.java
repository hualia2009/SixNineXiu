package com.ninexiu.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Gift;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.ThumbnailUtils;
import com.ninexiu.utils.Utils;

/**
 * 礼物之星排行
 * @author mao
 *
 */
public class GiftRankAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Gift> giftData;
	private AsyncImageLoader asyncImageLoader;
	private Bitmap bitmap;
	private Bitmap mDefaultImage;
	
	public GiftRankAdapter(Activity context,ArrayList<Gift> giftData) {
		this.mContext=context;
		this.giftData=giftData;
		bitmap = Utils.toRoundBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.loading_image),50,50,mContext);
		asyncImageLoader = new AsyncImageLoader();
		mDefaultImage = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.logo);
	}
	
	@Override
	public int getCount() {
		return giftData.size();
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
		final GiftViewHolder viewHolder;
		Gift gift=giftData.get(position);
		if(convertView==null){
			viewHolder=new GiftViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.gift_rank_list_item_layout, null);
			viewHolder.ivGift=(ImageView) convertView.findViewById(R.id.iv_gift_img);
			viewHolder.tvGiftName=(TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_user_level);
			viewHolder.tvGiftNum=(TextView) convertView.findViewById(R.id.tv_gift_num);
			viewHolder.giftRank=(ImageView) convertView.findViewById(R.id.iv_rank);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(GiftViewHolder) convertView.getTag();
		}
		viewHolder.tvGiftName.setText(gift.getReceiveName());
		viewHolder.tvGiftNum.setText(gift.getGiftCount()+"个");
//		bitmapLoader.display(viewHolder.ivGift, gift.getIconUrl());
		
		ThumbnailUtils.loadImage(gift.getIconUrl(), mDefaultImage, ApplicationEx.get().getBitmapLoader(), 
				viewHolder.ivGift, 50, 50, 0, true);
		
//		FinalBitmap.from(mContext).display(viewHolder.ivGift, gift.getIconUrl(),
//				Utils.toRoundBitmap(bitmap, 50, 50, mContext));
		/*Bitmap map=asyncImageLoader.loadBitmap(gift.getIconUrl(), new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
				viewHolder.ivGift.setImageBitmap(Utils.toRoundBitmap(imageDrawable,50,50,mContext));
			}
		});
		if(map==null){
			viewHolder.ivGift.setImageBitmap(bitmap);
		}else{
			viewHolder.ivGift.setImageBitmap(Utils.toRoundBitmap(map,50,50,mContext));
		}*/
		int credit=0;
		try {
			credit = Utils.getCredit(Long.parseLong(gift.getCredit()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		viewHolder.ivLevel.setImageResource(mContext.getResources().getIdentifier("host_level_"+credit, "drawable","com.ninexiu.nineshow"));
		if(position==0){
			viewHolder.giftRank.setImageResource(R.drawable.rank_one);
		}else if(position==1){
			viewHolder.giftRank.setImageResource(R.drawable.rank_two);
		}else if(position==2){
			viewHolder.giftRank.setImageResource(R.drawable.rank_three);
		}else{
			viewHolder.giftRank.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class GiftViewHolder{
		public ImageView ivGift;
		public TextView tvGiftName;
		public ImageView ivLevel;
		public TextView tvGiftNum;
		public ImageView giftRank;
	}

}
