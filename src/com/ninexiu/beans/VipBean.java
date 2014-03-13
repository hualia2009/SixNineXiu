package com.ninexiu.beans;

import java.util.ArrayList;

/**
 * 商场 类
 * @author mao
 *
 */
public class VipBean {
	
	private String VipName;
	private String AristocratName;
	private ArrayList<Vip> VipData;
	private String vipType;
	
	public class Vip{
		
		private String month;
		private String price;
		
		public String getMonth() {
			return month;
		}
		
		public void setMonth(String month) {
			this.month = month;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
	}
	
	public String getVipName() {
		return VipName;
	}
	
	public void setVipName(String vipName) {
		VipName = vipName;
	}
	
	public ArrayList<Vip> getVipData() {
		return VipData;
	}
	
	public void setVipData(ArrayList<Vip> vipData) {
		VipData = vipData;
	}
	
	public String getVipType() {
		return vipType;
	}
	
	public void setVipType(String vipType) {
		this.vipType = vipType;
	}
	
	public String getAristocratName() {
		return AristocratName;
	}
	
	public void setAristocratName(String aristocratName) {
		AristocratName = aristocratName;
	}
}
