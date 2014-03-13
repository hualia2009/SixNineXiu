package com.ninexiu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Task;
import com.ninexiu.sixninexiu.R;

/*
 * 新手任务适配器
 */
public class TaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Task> data;
	
	public TaskAdapter(Context context,ArrayList<Task> taskData) {
		this.context=context;
		this.data=taskData;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Task getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TaskViewHolder viewHolder;
		Task task=data.get(position);
		viewHolder=new TaskViewHolder();
		if(convertView==null){
			
			convertView=LayoutInflater.from(context).inflate(R.layout.task_list_item,null);
			viewHolder.btReward=(Button) convertView.findViewById(R.id.bt_task_reward);
			viewHolder.ivArrow=(ImageView) convertView.findViewById(R.id.iv_task_arrow);
			viewHolder.ivIsComplete=(ImageView) convertView.findViewById(R.id.iv_task_is_complete);
			viewHolder.tvDesciption=(TextView) convertView.findViewById(R.id.tv_task_descrption);
			viewHolder.tvIsComplete=(TextView) convertView.findViewById(R.id.tv_task_is_complete);
			viewHolder.tvReward=(TextView) convertView.findViewById(R.id.tv_task_reward);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(TaskViewHolder) convertView.getTag();
		}
		if(Integer.valueOf(task.getReceiver())==0){
			viewHolder.btReward.setText("领币");
			viewHolder.btReward.setBackgroundResource(R.drawable.task_unreward);
		}else{
			viewHolder.btReward.setText("已领币");
			viewHolder.btReward.setBackgroundResource(R.drawable.task_reward);
		}
		if(Integer.valueOf(task.getComplete())>=Integer.valueOf(task.getTotal())){
			viewHolder.ivIsComplete.setImageResource(R.drawable.task_complte);
			viewHolder.tvIsComplete.setText("已完成");
			viewHolder.btReward.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ivIsComplete.setImageResource(R.drawable.task_uncomplte);
			viewHolder.tvIsComplete.setText("未完成");
			viewHolder.btReward.setVisibility(View.GONE);
		}
		
		if(position==0||position==1){
			viewHolder.ivArrow.setVisibility(View.GONE);
		}else{
			viewHolder.ivArrow.setVisibility(View.VISIBLE);
		}
		viewHolder.tvDesciption.setText(task.getTaskName());
		viewHolder.tvReward.setText(task.getReward()+"九币");
		return convertView;
	}
	
	class TaskViewHolder{
		public TextView tvDesciption;//任务描述
		public ImageView ivIsComplete;//图片表示是否完成 
		public ImageView ivArrow;//任务跳转
		public TextView tvIsComplete;//表示是否完成
		public Button btReward;//是否已经领取奖励
		public TextView tvReward;  //任务奖励的金额
		
	}

}
