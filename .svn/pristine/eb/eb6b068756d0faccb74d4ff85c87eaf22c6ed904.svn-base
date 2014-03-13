package com.ninexiu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ninexiu.beans.Song;
import com.ninexiu.sixninexiu.R;

public class SongOrderdAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Song> datas;
	public SongOrderdAdapter(Context context,ArrayList<Song> datas) {
		this.mContext=context;
		this.datas=datas;
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
		SongOrderHolder orderHolder;
		Song song=datas.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.room_ordered_song_item,null);
			orderHolder=new SongOrderHolder();
			orderHolder.tvIndex=(TextView) convertView.findViewById(R.id.tv_ordered_song_index);
			orderHolder.tvName=(TextView) convertView.findViewById(R.id.tv_ordered_song_name);
			orderHolder.tvStatus=(TextView) convertView.findViewById(R.id.tv_ordered_song_status);
			convertView.setTag(orderHolder);
		}else{
			orderHolder=(SongOrderHolder) convertView.getTag();
		}
		orderHolder.tvIndex.setText(position+1+". "+song.getMusicName());
		orderHolder.tvName.setText(song.getOrderName());
		if(song.getStatus().equals("1")){
			orderHolder.tvStatus.setText("等待");
		}else{
			orderHolder.tvStatus.setText("同意");
		}
		return convertView;
	}

	class SongOrderHolder{
		public TextView tvIndex;
		public TextView tvName;
		public TextView tvStatus;
	}
}
