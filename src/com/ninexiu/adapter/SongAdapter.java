package com.ninexiu.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ninexiu.beans.Song;
import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.Utils;

/**
 * 歌曲适配器
 * @author mao
 *
 */
public class SongAdapter extends BaseAdapter {

	private ArrayList<Song> data;
	private User mUser; //用户信息
	private Activity context;
	private String roomId;
	private Dialog dialog;
	private int orderMoney;
	public SongAdapter(Activity context,ArrayList<Song> songData,User user,String rid,int money){
		this.data=songData;
		this.context=context;
		this.mUser=user;
		this.roomId=rid;
		this.orderMoney=money;
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
		SongViewHolder holder;
		final Song song=data.get(position);
		if(convertView==null){
			holder=new SongViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.room_order_song_item, null);
			holder.tvSongIndex=(TextView) convertView.findViewById(R.id.tv_song_index);
			holder.tvSongName=(TextView) convertView.findViewById(R.id.tv_song_name);
			holder.btnOrder=(Button) convertView.findViewById(R.id.bt_order_song);
			convertView.setTag(holder);
		}else{
			holder=(SongViewHolder) convertView.getTag();
		}
		holder.tvSongIndex.setText((position+1)+".");
		holder.tvSongName.setText(song.getMusicName()+"-"+song.getOriginal());
		holder.btnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mUser != null) {
					if (mUser.getUserCoin() >= orderMoney || (Integer.valueOf(mUser.getUserDian()) >= orderMoney)) {
						Utils.showToSomeDialog(context, song.getMusicName(), mUser, roomId);
					} else {
						Utils.showChargeDialog(context);
					}
				}
			}
		});
		return convertView;
	}
	
	
	class SongViewHolder {
		public TextView tvSongIndex;
		public TextView tvSongName;
		public Button btnOrder;
	}
	
	

}
