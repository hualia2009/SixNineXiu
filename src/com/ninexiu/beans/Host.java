package com.ninexiu.beans;

import java.io.Serializable;

/*
 * 主播类
 */
public class Host extends Father implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostImage; // 主播头像
	private String hostAttId; //根据这个ID 来取消关注主播
	//room_ext1=0:  rtmp://videodownls.9xiu.com:1935/9xiu/123
	//room_ext1=1:  rtmp://videodownws.9xiu.com:1935/9xiu/123

	public String getHostAttId() {
		return hostAttId;
	}

	public void setHostAttId(String hostAttId) {
		this.hostAttId = hostAttId;
	}
	

	public String getHostImage() {
		return hostImage;
	}

	public void setHostImage(String hostImage) {
		this.hostImage = hostImage;
	}
}
