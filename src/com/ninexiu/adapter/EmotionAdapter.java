package com.ninexiu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.Utils;

/*
 * 聊天表情显示类
 */
public class EmotionAdapter extends BaseAdapter {

	public Context context;
	private ArrayList<Integer> ids;
	private ArrayList<String> iconStrs;
	public int size=AppConstants.EMOTION_ID.length;
	
	public EmotionAdapter(Context context, int i) {
		this.context=context;
		ids=new ArrayList<Integer>();
		iconStrs=new ArrayList<String>();
		for (int j = i*21; j < (i+1)*21&&j<size; j++) {
			ids.add(j);
			iconStrs.add("[#imgface"+(j+1)+"#]");
		}
	}
	
	
	@Override
	public int getCount() {
		return ids.size();
	}

	@Override
	public Object getItem(int position) {
		return iconStrs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ids.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			ImageView iv = new ImageView(context);
			int width = Utils.dip2px(context, 30);
			iv.setLayoutParams(new AbsListView.LayoutParams(width,width));
			iv.setScaleType(ScaleType.FIT_XY);
			convertView=iv;//LayoutInflater.from(context).inflate(R.layout.emotion_grid_item, null);
		}
		ImageView iv  = (ImageView)convertView;
		iv.setImageResource(AppConstants.EMOTION_ID[ids.get(position)]);
		return convertView;
	}

}
