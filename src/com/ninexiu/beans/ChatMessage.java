package com.ninexiu.beans;

/**
 * 聊天消息类
 * 
 * @author Administrator
 * 
 */
public class ChatMessage {

	private String sendTime;// 消息发送的时间

	private String sendNickName; // 消息发送的用户

	private String sendAction; // 发送的动作

	private String toNickName;// 接受人名字

	private String messageContent;// 消息的内容
	
	private boolean isReward;//是否是中奖显示
	
	private boolean isNotice;//是否是系统通知
//	private String  rewardtype;//几倍大奖
//	
//	private String reward;//奖励的总数
//	
//	private String rewardDes;
	
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendNickName() {
		return sendNickName;
	}

	public void setSendNickName(String sendNickName) {
		this.sendNickName = sendNickName;
	}

	public String getSendAction() {
		return sendAction;
	}

	public void setSendAction(String sendAction) {
		this.sendAction = sendAction;
	}

	public String getToNickName() {
		return toNickName;
	}

	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}


	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public boolean isReward() {
		return isReward;
	}

	public void setReward(boolean isReward) {
		this.isReward = isReward;
	}

	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}
}
