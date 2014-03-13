package com.ninexiu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Host;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

public class RecommandAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Host> datas;
	private int width;
	private View recommand;
	private AsyncImageLoader asyncImageLoader;
	
	public RecommandAdapter(ChatRoomActivity context,ArrayList<Host> datas, View videoRecomd) {
		this.context=context;
		this.datas=datas;
		width=context.getResources().getDisplayMetrics().widthPixels/3;
		this.recommand=videoRecomd;
		asyncImageLoader = new AsyncImageLoader();
	}
	
	@Override
	public int getCount() {
		return datas.size();
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
		final Host host=datas.get(position);
		final ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.video_recomd_grid_item, null);
			holder.parent=(FrameLayout) convertView.findViewById(R.id.frame_layout);
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tvCount=(TextView) convertView.findViewById(R.id.tv_count);
			holder.iv=new ImageView(context);
			FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(width*3/4, width*3/4);
			layoutParams.gravity = Gravity.CENTER;
			holder.iv.setScaleType(ImageView.ScaleType.FIT_XY);
			holder.iv.setLayoutParams(layoutParams);
			holder.parent.addView(holder.iv, 0);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		//eyan add 20131113
		/*ImageLoader.getInstance().displayImage(host.getHostImage(), holder.iv, new ImageLoadingListener(){

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				((ImageView)view).setImageBitmap(defaultBitmap);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
				((ImageView)view).setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});*/
//		com.ninexiu.utils.FinalBitmap.from(context).display(holder.iv, host.getHostImage(), R.drawable.loading_image);
		
		Bitmap map=asyncImageLoader.loadBitmap(host.getHostImage(), new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {

				holder.iv.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
			}
		});
		if(map==null){
			
				map = Utils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_image));
			holder.iv.setImageBitmap(map);
		}else{
			holder.iv.setImageBitmap(Utils.toRoundBitmap(map));
		}
		
		holder.tvName.setText(host.getNickName());
		holder.tvCount.setText(host.getAudice()+"人正在观看");
		holder.parent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recommand.setVisibility(View.GONE);
				Intent intent=new Intent(context,ChatRoomActivity.class);
				intent.putExtra("self", true);
				intent.putExtra("host", host);
				context.startActivity(intent);
				//jin removed 20131128
				//rtmpPlay.startPlay(host.getRoomTag(), host.getRoomId());
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		public FrameLayout parent;
		public TextView tvName;
		public TextView tvCount;
		public ImageView iv;
	}

}

