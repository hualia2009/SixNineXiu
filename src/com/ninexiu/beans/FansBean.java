package com.ninexiu.beans;

import java.util.ArrayList;

/**
 * 粉丝榜类
 * 
 * @author mao
 * 
 */
public class FansBean {

	ArrayList<Father> dayFan;
	ArrayList<Father> MonthFan;
	ArrayList<Father> SuperFan;

	public ArrayList<Father> getDayFan() {
		return dayFan;
	}

	public void setDayFan(ArrayList<Father> dayFan) {
		this.dayFan = dayFan;
	}

	public ArrayList<Father> getMonthFan() {
		return MonthFan;
	}

	public void setMonthFan(ArrayList<Father> monthFan) {
		MonthFan = monthFan;
	}

	public ArrayList<Father> getSuperFan() {
		return SuperFan;
	}

	public void setSuperFan(ArrayList<Father> superFan) {
		SuperFan = superFan;
	}

}
