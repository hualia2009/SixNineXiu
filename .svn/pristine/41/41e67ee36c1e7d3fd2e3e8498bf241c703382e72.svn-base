package com.ninexiu.sixninexiu;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.ninexiu.adapter.TaskAdapter;
import com.ninexiu.beans.Host;
import com.ninexiu.beans.Task;
import com.ninexiu.beans.User;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.NLog;
import com.ninexiu.utils.Utils;

/**
 * 新手任务界面
 * @author mao
 *
 */
public class NewTaskActivity extends BaseActivity {
	private User mUser;
	private Dialog mDialog;
	private PullListView lvTask;
	private ArrayList<Task> tasks=new ArrayList<Task>();
	private TaskAdapter taskAdapter;
	private ArrayList<Host> hosts; //直播大厅传过来的在线的主播列表
	private Random random;
	private Handler uiHanlder=new Handler(){
		public void handleMessage(android.os.Message msg) {
			getTaskReward((String) msg.obj);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task_layout);
		 mUser=ApplicationEx.get().getUserManager().getUser();
		 hosts=(ArrayList<Host>) getIntent().getSerializableExtra("live");
		 random=new Random();
		tvTitle.setText("做任务,赢九币");
		lvTask=(PullListView) findViewById(R.id.lv_task);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mUser!=null){
			getUserTaskList();
		}
	}

	
	/**
	 * 获取新手任务列表 
	 */
	private void getUserTaskList() {
		
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_NEW_TASK, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(NewTaskActivity.this, "任务列表获取中...");
				mDialog.show();
//				Bundle args=new Bundle();
//				args.putString("msg", "任务列表获取中...");
//				showDialog(SHOW_PROGRESS_DILAOG, args);
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONArray array=jsonObject.optJSONArray("retval");
							NLog.e("test", "新手任务表=="+array.toString());
							if(array!=null){
							tasks.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject3=array.optJSONObject(i);
								Task task=new Task();
								task.setTaskid(jsonObject3.optString("taskid"));
								task.setTaskName(jsonObject3.optString("desc"));
								task.setComplete(jsonObject3.optString("complate"));
								task.setReceiver(jsonObject3.optString("receive"));
								task.setReward(jsonObject3.optString("reward"));
								task.setTotal(jsonObject3.optString("total"));
								tasks.add(task);
							}
							taskAdapter=new TaskAdapter(NewTaskActivity.this, tasks);
							lvTask.setAdapter(taskAdapter);
							lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									final Task task1=(Task)parent.getAdapter().getItem(position);
									if(Utils.isNotEmptyString(task1.getReceiver())){
									if(!task1.getReceiver().equals("1")){
										if(Integer.valueOf(task1.getComplete())>=Integer.valueOf(task1.getTotal())){
											getTaskReward(task1.getTaskid());
										}else{
										switch (position){
										case 1://修改昵称
											AlertDialog.Builder builder=new AlertDialog.Builder(NewTaskActivity.this);
											builder.setTitle("修改昵称");
											final EditText editText=new EditText(NewTaskActivity.this);
											editText.setText(mUser.getNickName());
											builder.setView(editText);
											builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													if(!(editText.getText().toString().trim().equals(mUser.getNickName()))){
														doUpdateNickName(editText.getText().toString().trim(),task1.getTaskid());
													}else{
														Utils.MakeToast(getApplicationContext(), "修改昵称不能相同");
													}
												}
												
											});
											builder.setNegativeButton("取消", null);
											builder.show();
										break;
										case 2://累计送出10个元宝
											if(hosts.size()>0){
												Intent intent=new Intent(NewTaskActivity.this,ChatRoomActivity.class);
												intent.putExtra("fromtask", true);
												intent.putExtra("taskid", task1.getTaskid());
												intent.putExtra("host", hosts.get(random.nextInt(hosts.size())));
												startActivity(intent);
											}else{
												Utils.MakeToast(getApplicationContext(), "当前无在线主播");
											}
											
											break;

										case 3://首次关注一名主播
											if(hosts.size()>0){
												Intent intent2=new Intent(NewTaskActivity.this,ChatRoomActivity.class);
												intent2.putExtra("fromtask", true);
												intent2.putExtra("taskid", task1.getTaskid());
												intent2.putExtra("host", hosts.get(random.nextInt(hosts.size())));
												startActivity(intent2);
											}else{
												Utils.MakeToast(getApplicationContext(), "当前无在线主播");
											}
											break;
											
										case 4://心爱的主播送红玫瑰
											if(hosts.size()>0){
												Intent intent3=new Intent(NewTaskActivity.this,ChatRoomActivity.class);
												intent3.putExtra("taskid", task1.getTaskid());
												intent3.putExtra("fromtask", true);
												intent3.putExtra("host", hosts.get(random.nextInt(hosts.size())));
												startActivity(intent3);
											}else{
												Utils.MakeToast(getApplicationContext(), "当前无在线主播");
											}
											break;
										case 5: //充值任意金额
											Intent intent4=new Intent(NewTaskActivity.this,ChargeActivity.class);
											intent4.putExtra("fromtask", true);
											intent4.putExtra("taskid", task1.getTaskid());
											startActivity(intent4);
											break;
											
										case 6://充值500
											Intent intents=new Intent(NewTaskActivity.this,ChargeActivity.class);
											intents.putExtra("fromtask", true);
											intents.putExtra("taskid", task1.getTaskid());
											intents.putExtra("taskid2", tasks.get(5).getTaskid());
											startActivity(intents);
											break;
										}
									}
								}else{
									Utils.MakeToast(getApplicationContext(), "已领取奖励,不能重复领取");
								}
									}
								}
							});
							}else{
								String msg=jsonObject.optString("msg");
								Utils.MakeToast(getApplicationContext(), msg);
							}
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
				
			}
		});
	}
	
	/**
	 * 领取任务奖励
	 * @param taskid 
	 */
	private void getTaskReward(String taskid) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("taskId",taskid);
		asyncHttpClient.post(AppConstants.GET_NEW_TASK_REWARD, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(NewTaskActivity.this, "领取奖励中...");
				mDialog.show();
//				Bundle args=new Bundle();
//				args.putString("msg", "领取奖励中...");
//				showDialog(SHOW_PROGRESS_DILAOG, args);
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							Utils.MakeToast(getApplicationContext(), "领币成功");
							getUserTaskList();
							Utils.UpdateUserInfo(mUser, NewTaskActivity.this);
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
			
		});
		
	}
	
	
	/**
	 * 修改用户昵称
	 * @param string 
	 * @param taskId 
	 */
	private void doUpdateNickName(String nickName, final String taskId) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("nickname", nickName);
		asyncHttpClient.post(AppConstants.UPDATE_USER_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {
					JSONObject jsonObject=new JSONObject(content);
					boolean isSuccess=jsonObject.getBoolean("success");
					if(isSuccess==true){
						Utils.doUserTask(mUser, taskId,uiHanlder);
						JSONObject jsonObject2=jsonObject.getJSONObject("retval");
						if(jsonObject2!=null){
							ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
							//本地缓存用户信息
//							Utils.saveUser(NewTaskActivity.this, user);
						}
					}else{
						String msg=jsonObject.optString("msg");
						Utils.MakeToast(getApplicationContext(), msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}
}
