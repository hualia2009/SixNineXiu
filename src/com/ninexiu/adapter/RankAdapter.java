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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Father;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

/**
 * 排行榜适配器
 * @author mao
 *
 */
public class RankAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Father> data;
	private boolean isStar;
	private Bitmap defaultBitmap;
	
	private AsyncImageLoader asyncImageLoader;
	public RankAdapter(Activity context,ArrayList<Father> data,boolean star){
		this.context=context;
		this.data=data;
		this.isStar=star;
		asyncImageLoader = new AsyncImageLoader();
	}
	@Override
	public int getCount() {
		if(data!=null){
			return data.size();
		}
		return 0;
		
	}

	@Override
	public Father getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RankViewHolder holder;
		final Father father=data.get(position);
		if(convertView==null){
			holder=new RankViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.host_att_list_item, null);
			holder.ivHead=(ImageView) convertView.findViewById(R.id.iv_att_head);
			holder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_host_level);
			holder.ivIndex=(ImageView) convertView.findViewById(R.id.iv_rank_index);
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_nickname);
			holder.tvNum=(TextView) convertView.findViewById(R.id.tv_liang);
			holder.btCancle=(Button) convertView.findViewById(R.id.bt_cancel);
			holder.btCancle.setVisibility(View.GONE);
			convertView.setTag(holder);
		}else{
			holder=(RankViewHolder) convertView.getTag();
		}
		if(position==0||position==1||position==2){
			holder.ivIndex.setVisibility(View.VISIBLE);
			switch (position) {
			case 0:
				holder.ivIndex.setImageResource(R.drawable.rank_one);
				break;

			case 1:
				holder.ivIndex.setImageResource(R.drawable.rank_two);
				break;
			case 2:
				holder.ivIndex.setImageResource(R.drawable.rank_three);
				break;
			}
		}else{
			holder.ivIndex.setVisibility(View.GONE);
		}
		Bitmap map=asyncImageLoader.loadBitmap(father.getAvatar(), new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {

				holder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
			}
		});
		if(map==null){
			if(defaultBitmap == null)
				defaultBitmap = Utils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_image));
			holder.ivHead.setImageBitmap(defaultBitmap);
		}else{
			holder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
		}
		
		if(isStar){
			Utils.setHostLevel(father.getCredit(), holder.ivLevel);
		}else{
			if(Utils.isNotEmptyString(father.getUserType())){
				if(father.getUserType().equals("1")){
					Utils.setHostLevel(father.getCredit(), holder.ivLevel);
				}else{
					Utils.setUserLevel(father.getWeath(), holder.ivLevel);
				}
			}
		}
		holder.tvName.setText(father.getNickName());
		holder.tvNum.setText(father.getUserNum());
		return convertView;
	}
	
	public class RankViewHolder{
		public ImageView ivHead;
		public ImageView ivLevel;
		public ImageView ivIndex;
		public TextView tvName;
		public TextView tvNum;
		public Button btCancle;
	}

}
