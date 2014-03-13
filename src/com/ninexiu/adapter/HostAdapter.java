package com.ninexiu.adapter;

import java.util.ArrayList;

import com.ninexiu.sixninexiu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HostAdapter extends BaseAdapter{
	
	private ArrayList<String> hostName;
	private Context context;
	
	public HostAdapter(Context context, ArrayList<String> hostName){
		this.context = context;
		this.hostName = hostName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(hostName!=null){
			return hostName.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HostTextHolder hostTextHolder;
		if (convertView==null) {
			hostTextHolder = new HostTextHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.host_text_item, null);
			hostTextHolder.host = (TextView) convertView.findViewById(R.id.host_text);
			convertView.setTag(hostTextHolder);
		}
		else {
			hostTextHolder = (HostTextHolder) convertView.getTag();
		}
		hostTextHolder.host.setText(hostName.get(position));
		
		return convertView;
	}
	class HostTextHolder {
		public TextView host;
	}

}
