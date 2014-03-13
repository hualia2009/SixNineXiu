package com.ninexiu.beans;

import java.io.Serializable;

/*
 * 用户类
 */
public class User extends Father implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token; // 服务器端的session
	private String userDetail; //包括用户的所有详细信息,为用户入场发信息用
	private int userCoin;//九币
	private int userDian; //九点
	private String sex; //性别  1.男 2. 女
	private String provice; //所在省份
	private String city;//所在城市
	private boolean isNobel;//判断是否是贵族
	private String user_props;
	
	
	public String getUser_props() {
		return user_props;
	}

	public void setUser_props(String user_props) {
		this.user_props = user_props;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getUserCoin() {
		return userCoin;
	}

	public void setUserCoin(int userCoin) {
		this.userCoin = userCoin;
	}

	public int getUserDian() {
		return userDian;
	}

	public void setUserDian(int userDian) {
		this.userDian = userDian;
	}

	public String getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isNobel() {
		return isNobel;
	}

	public void setNobel(boolean isNobel) {
		this.isNobel = isNobel;
	}
}
