package com.ninexiu.beans;

/*
 * 任务系统Bean
 */
public class Task {
	
	private String taskName;// 任务名称
	private String complete;// 完成数
	private String receiver;// 是否领币 1：代表领币 0 代表未领币
	private String total; // 总任务
	private String taskid;// 任务ID
	private String reward;// 奖励的九币数

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

}
