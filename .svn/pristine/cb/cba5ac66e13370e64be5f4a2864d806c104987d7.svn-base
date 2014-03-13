package com.ninexiu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninexiu.beans.Father;
import com.ninexiu.sixninexiu.R;

public class ToChatAdapter extends BaseAdapter {

	private ArrayList<Father> mData;
	private Context context;
	
	public ToChatAdapter(Context context,ArrayList<Father> fatherData) {
		this.mData=fatherData;
		this.context=context;
	}
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public String getItem(int position) {
		return mData.get(position).getDetail();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Father father=mData.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.room_pop_chat_to_item, null);
		}
		TextView tv=(TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(father.getNickName());
		return convertView;
	}

}
