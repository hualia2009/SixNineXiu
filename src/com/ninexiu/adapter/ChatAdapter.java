package com.ninexiu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ninexiu.beans.ChatMessage;
import com.ninexiu.beans.Father;
import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.LoginDialogActivity;
import com.ninexiu.sixninexiu.OnlyLoginActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.RegActivity;
import com.ninexiu.utils.Utils;

/**
 * 显示聊天信息的适配器
 * @author mao
 *
 */
public class ChatAdapter extends BaseAdapter {

	private ArrayList<ChatMessage> datas;
	private ChatRoomActivity mContext;
//	private int Chatface;
	private List<Father> userData;
	private List<Father> Manageruser;
	private View parentView;
	private User mUser;
	private Father mHost;//房间ID
	private int[] userMenuId=new int[]{R.id.room_mem_name,R.id.chat_btn,R.id.send_gift_btn,
			R.id.shut_up_btn,R.id.kick_btn};
	private TextView[] tvMenu=new TextView[userMenuId.length];
	
	public ChatAdapter(ArrayList<ChatMessage> messages,ChatRoomActivity context,View parentView,List<Father> allUserData,ArrayList<Father> mMangerUser,Father mHost,User mUser){
		this.datas=messages;
		this.Manageruser=mMangerUser;
		this.mContext=context;
		this.mUser=mUser;
//		Chatface=(int) context.getResources().getDimension(R.dimen.chat_panle_face_width_height);
		this.parentView=parentView;
		this.userData=allUserData;
		this.mHost=mHost;
		
	}
	public void setUser(User mUser){
		this.mUser=mUser;
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ChatMessage chatMessage=datas.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.room_chat_list_item, null);
			viewHolder.tvMessage=(TextView) convertView.findViewById(R.id.tv_message);
			viewHolder.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		SpannableStringBuilder builder=new SpannableStringBuilder();
		String time=chatMessage.getSendTime();
		builder.append(time);//消息发送的时候
		builder.append(" ");
		int start=time.length()+1;
		int end=0;
		if(chatMessage.getSendNickName()!=null){
			String sendName=chatMessage.getSendNickName();
			builder.append(sendName);
			builder.append(" ");
			end=start+sendName.length()+1;
			ClickButton button=new ClickButton(new SubMenu(sendName));
//			builder.setSpan(new BubbleSpan(), start-1, end-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(button, start-1, end-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		}else{
			end=start;
		}
		if(chatMessage.getSendAction()!=null){
			builder.append(chatMessage.getSendAction());
		}
		int start1=0;
		if(chatMessage.getSendAction()!=null){
			start1=end+chatMessage.getSendAction().length();
		}else{
			start1=end;
		}
		
		int end1=0;
		builder.append(" ");
		String toNickName=chatMessage.getToNickName();
		if(toNickName!=null){
			builder.append(toNickName);
			end1=start1+1+toNickName.length();
			ClickButton button2=new ClickButton(new SubMenu(toNickName));
			builder.setSpan(button2, start1,end1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		/*if(Utils.isNotEmptyString(chatMessage.getRewardtype())){
			//"送"+giftNames+"[#mgift"+giftIds+"#]"+countss+"个,喜中"+rewardType+"倍大奖"+rewardtime+"次"+"奖励"+reward+"九点");
			String title=chatMessage.getMessageContent();
			int starts=end1+title.length();
			String title1=chatMessage.getRewardtype();
			int ends=starts+title1.length();
			String title3=chatMessage.getRewardDes();
			String title4="奖励";
			String title5=chatMessage.getReward();
			String title6="九点";
			builder.append(title);
			builder.append(title1);
			ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.RED);
			builder.setSpan(colorSpan, starts, ends, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.append(title3);
			builder.append(title4);
			builder.append(title5);
			int start2=ends+title3.length()+title4.length();
			ForegroundColorSpan colorSpan2=new ForegroundColorSpan(Color.RED);
			builder.setSpan(colorSpan2, start2, start2+title5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.append(title6);
//			builder.append(chatMessage.getMessageContent()).append(chatMessage.getRewardtype()).append(chatMessage.getRewardDes()).append("奖励")
		}else{*/
		String msg=chatMessage.getMessageContent();
		if(chatMessage.isNotice()){
			viewHolder.tvMessage.setTextColor(Color.parseColor("#739a45"));
			if(msg.indexOf("注册或登录")!=-1){
				SpannableStringBuilder builder2=new SpannableStringBuilder(msg);
				builder2.setSpan(new LoginButton(new LoginListener(1)), 20, 22, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				builder2.setSpan(new LoginButton(new LoginListener(2)), 23,25, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				viewHolder.tvMessage.setText(builder2);
			}else{
				viewHolder.tvMessage.setText(msg);
			}
		}else{
			viewHolder.tvMessage.setTextColor(Color.BLACK);
			String zhengze = "\\[#\\w+#\\]"; // 正则表达式，用来判断消息内是否有表情
			try {
				SpannableStringBuilder spannableString =Utils.getExpressionString(mContext, msg, zhengze);
				if(chatMessage.isReward()){
					ForegroundColorSpan colorSpan2=new ForegroundColorSpan(Color.RED);
					builder.append(spannableString);
					builder.setSpan(colorSpan2, end1, end1+spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}else{
					builder.append(spannableString);
				}
				viewHolder.tvMessage.setText(builder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(position==getCount()-1){
			convertView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha));
		}
		return convertView;
	}
	
//	public class BubbleSpan extends DynamicDrawableSpan {
//		  @Override
//		  public Drawable getDrawable() {
//		    Resources res = mContext.getResources();
//		    Drawable d = res.getDrawable(R.drawable.shadowright);
//		    d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
//		    return d;
//		  }
//		}
	

	class SubMenu implements View.OnClickListener{
		
		private String name;
		public SubMenu(String name) {
			this.name=name;
		}

		@Override
		public void onClick(View v) {
				View contentView=LayoutInflater.from(mContext).inflate(R.layout.room_mem_menu, null);
				PopupWindow popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
				popupWindow.setAnimationStyle(R.style.popShowGiftAnimStyle);
				popupWindow.showAtLocation(parentView, Gravity.BOTTOM|Gravity.LEFT, 0,0);
				for (int i = 0; i < userMenuId.length; i++) {
					tvMenu[i]=(TextView) contentView.findViewById(userMenuId[i]);
					if(i==0){
						tvMenu[i].setText(name);
					}else{
						tvMenu[i].setOnClickListener(new MenuListener(name,popupWindow));
					}
				}
		}
		
	}
	
	/**
	 * 弹出用户菜单之后 点击其中的选项
	 * @author mao
	 *
	 */
	class MenuListener implements View.OnClickListener{

		private Father father;
		private PopupWindow popupWindow;
		public MenuListener(String name,PopupWindow popWindow) {
			this.popupWindow=popWindow;
			for (int i = 0; i <userData.size(); i++) {
				Father father=userData.get(i);
				if(father.getNickName().equals(name)){
					this.father=father;
					break;
				}
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.chat_btn: //与他聊天
				if(mUser!=null){
					if(father!=null){
						mContext.doChatWith(father);
					}
				}else{
					Intent intent=new Intent(mContext,LoginDialogActivity.class);
//					intent.putExtra("fromChatRoom", true);
					mContext.startActivity(intent);
				}
				break;

			case R.id.send_gift_btn: //送她礼物
				if(mUser!=null){
					if(father!=null){
						mContext.showPopGift();
						if(!mContext.mToGiftData.contains(father)&&!(father.getUid().equals(mHost.getUid()))){
							mContext.mToGiftData.add(father);
						}
						mContext.tvTo.setText(father.getNickName());
						mContext.toUserId=Integer.valueOf(father.getUid());
					}
				}else{
					Intent intent=new Intent(mContext,LoginDialogActivity.class);
//					intent.putExtra("fromChatRoom", true);
					mContext.startActivity(intent);
				}
				
				break;
//				asdadsd
			case R.id.shut_up_btn: //禁言
				if(mUser!=null){
					if(father!=null){
						if("2".equals(mUser.getUserType())||"3".equals(mUser.getUserType())){
							doShutTask("10",father.getUid(),father.getNickName());
						}else{
							if(Manageruser.contains(mUser)){
								if(!Utils.isVip(father.getVipType())){
									doShutTask("10",father.getUid(),father.getNickName());
								}else{
									Utils.MakeToast(mContext, "不能禁言VIP用户");
								}
							}else{
								Utils.MakeToast(mContext, "只有管理员才可以禁言");
							}
						}
					}
				}else{
					Intent intent=new Intent(mContext,LoginDialogActivity.class);
//					intent.putExtra("fromChatRoom", true);
					mContext.startActivity(intent);
				}
				break;
				
			case R.id.kick_btn://剔除房间
				if(mUser!=null){
					if(father!=null){
						if("2".equals(mUser.getUserType())||"3".equals(mUser.getUserType())){
							doShutTask("9",father.getUid(),father.getNickName());
						}else{
							if(Manageruser.contains(mUser)){
								if(!Utils.isVip(father.getVipType())){
									doShutTask("9",father.getUid(),father.getNickName());
								}else{
									Utils.MakeToast(mContext, "不能踢VIP用户");
								}
							}else{
								Utils.MakeToast(mContext, "只有管理员才可以踢人");
							}
						}
					}
				}else{
					Intent intent=new Intent(mContext,LoginDialogActivity.class);
//					intent.putExtra("fromChatRoom", true);
					mContext.startActivity(intent);
				}
				break;
			}
			popupWindow.dismiss();
		}
		
	}
	
	protected void doShutTask(String type,String toUid,String toNickName) {
//			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
//			RequestParams params=new RequestParams();
//			params.put("uid", mUser.getUid());
//			params.put("token", mUser.getToken());
//			params.put("rid", mHost.getRoomId());
//			params.put("type", type);
//			params.put("touid", toUid);
//			params.put("tonickname", toNickName);
//			asyncHttpClient.post(AppConstants.USER_CONTROL, params, new AsyncHttpResponseHandler(){
//				
//			});
		this.mContext.sendMsgToServer(type, getData(mUser.getUid(), toUid, mHost.getRoomId()));
	}
	public JSONObject getData(String uid, String touid, String rid) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("uid", uid);
			jo.put("touid", touid);
			jo.put("rid", rid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
	
	class ViewHolder{
		TextView tvMessage;
	}
	
	class ClickButton extends ClickableSpan{

		  private final View.OnClickListener mListener;
		  public ClickButton(View.OnClickListener l){
		  mListener = l;
		  }
		  
		  @Override
		  public void onClick(View v){
		  mListener.onClick(v);
		  
		  }
		  @Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setColor(Color.parseColor("#9D409D"));
			ds.setUnderlineText(false);
		}
		
	}
	
	class LoginButton extends ClickableSpan{

		private final View.OnClickListener mListener;
		  public LoginButton(View.OnClickListener l){
		  mListener = l;
		  }
		@Override
		public void onClick(View widget) {
			mListener.onClick(widget);
		}
		  @Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setColor(Color.BLUE);
			}
			
	}
	
	class LoginListener implements View.OnClickListener{
		private int tag;
		public LoginListener(int i) {
			this.tag=i;
		}

		@Override
		public void onClick(View v) {
			if(tag==1){
				Intent intent=new Intent(mContext,RegActivity.class);
				intent.putExtra("fromChatRoom", true);
				mContext.startActivity(intent);
			}else{
				Intent intent=new Intent(mContext,OnlyLoginActivity.class);
				intent.putExtra("fromChatRoom", true);
				mContext.startActivity(intent);
			}
		}
	}
	

}
