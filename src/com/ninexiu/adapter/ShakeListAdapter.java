package com.ninexiu.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Host;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

/**
 *   摇一摇适配器
 * @author mao
 *
 */
public class ShakeListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Host> datas;
	private AsyncImageLoader asyncImageLoader;
	private JSONObject jsonImpressObject;
	public ShakeListAdapter(Context context,ArrayList<Host> hostData,JSONObject impressObject){
		this.context=context;
		this.jsonImpressObject=impressObject;
		this.datas=hostData;
		asyncImageLoader=new AsyncImageLoader();
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
		 final ShakeViewHolder viewHolder;
		 final Host host=datas.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.shake_list_item, null);
			viewHolder=new ShakeViewHolder();
			viewHolder.ivHead=(ImageView) convertView.findViewById(R.id.iv_host_head);
			viewHolder.tvName=(TextView) convertView.findViewById(R.id.tv_host_name);
			viewHolder.tvCharOne=(TextView) convertView.findViewById(R.id.tv_char_one);
			viewHolder.tvCharTwo=(TextView) convertView.findViewById(R.id.tv_char_two);
			viewHolder.tvCharThree=(TextView) convertView.findViewById(R.id.tv_char_three);
			viewHolder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_host_level);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ShakeViewHolder) convertView.getTag();
		}
		viewHolder.ivHead.setTag(host.getHostImage());
		Bitmap map=asyncImageLoader.loadBitmap(host.getHostImage(), new AsyncImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
				String tag=(String) viewHolder.ivHead.getTag();
				if(tag.equals(imageUrl)){
					viewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
				}
			}
		});
		if(map==null){
			viewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_image)));
		}else{
			viewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
		}
		viewHolder.tvName.setText(host.getNickName());
		Utils.setHostLevel(host.getCredit(), viewHolder.ivLevel);
		String xingGeStr=host.getImpress();
//		Log.e("xingGe", xingGeStr); //	"1|23|45"
		if(jsonImpressObject!=null){
			if(Utils.isNotEmptyString(xingGeStr)&&!xingGeStr.equals("null")){
				int index=xingGeStr.indexOf("|");
			if(index!=-1){
				viewHolder.tvCharOne.setText(jsonImpressObject.optString(xingGeStr.substring(0,index)));
				int lastIndex=xingGeStr.lastIndexOf("|");
				if(lastIndex!=-1&&lastIndex!=index){
					viewHolder.tvCharTwo.setText(jsonImpressObject.optString(xingGeStr.substring(index+1, lastIndex)));
					viewHolder.tvCharThree.setText(jsonImpressObject.optString(xingGeStr.substring(lastIndex+1, xingGeStr.length())));
				}else{
					viewHolder.tvCharTwo.setText(jsonImpressObject.optString(xingGeStr.substring(index+1, xingGeStr.length())));
					viewHolder.tvCharThree.setVisibility(View.GONE);
				}
				
			}else{
				viewHolder.tvCharOne.setText(jsonImpressObject.optString(xingGeStr));
				viewHolder.tvCharTwo.setVisibility(View.GONE);
				viewHolder.tvCharThree.setVisibility(View.GONE);
				
			}
			}else{
				viewHolder.tvCharOne.setVisibility(View.GONE);
				viewHolder.tvCharTwo.setVisibility(View.GONE);
				viewHolder.tvCharThree.setVisibility(View.GONE);
			}
		}
		viewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,ChatRoomActivity.class);
				intent.putExtra("host", host);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class ShakeViewHolder{
		public ImageView ivHead;
		public TextView tvName;
		public TextView tvCharOne;
		public TextView tvCharTwo;
		public TextView tvCharThree;
		public TextView tvShare;
		public ImageView ivLevel;
	}

}
