package com.ninexiu.beans;

import java.util.ArrayList;

/**
 * 排行类
 * 
 * @author mao
 * 
 */
public class RankBean {

	private ArrayList<Father> dayRank; // 日榜
	private ArrayList<Father> weekRank; // 周榜
	private ArrayList<Father> monthRank; // 月榜
	private ArrayList<Father> superRank; // 超级棒

	public ArrayList<Father> getDayRank() {
		return dayRank;
	}

	public void setDayRank(ArrayList<Father> dayRank) {
		this.dayRank = dayRank;
	}

	public ArrayList<Father> getWeekRank() {
		return weekRank;
	}

	public void setWeekRank(ArrayList<Father> weekRank) {
		this.weekRank = weekRank;
	}

	public ArrayList<Father> getMonthRank() {
		return monthRank;
	}

	public void setMonthRank(ArrayList<Father> monthRank) {
		this.monthRank = monthRank;
	}

	public ArrayList<Father> getSuperRank() {
		return superRank;
	}

	public void setSuperRank(ArrayList<Father> superRank) {
		this.superRank = superRank;
	}

}
