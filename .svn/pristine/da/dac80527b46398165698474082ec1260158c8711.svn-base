package com.ninexiu.utils;

import java.util.Comparator;

import com.ninexiu.beans.Father;

/*
 * 十富以上和十富以下的用户排序
 */
public class VipSort implements Comparator<Father> {

	@Override
	public int compare(Father lhs, Father rhs) {
		String first=lhs.getVipType();
		String next=rhs.getVipType();
		if(Utils.isNotEmptyString(first)&&Utils.isNotEmptyString(next)){
			if(Integer.valueOf(first)>Integer.valueOf(next)){
				return -1;
			}else if(Integer.valueOf(first)<Integer.valueOf(next)){
				return 1;
			}else{
				if(Long.valueOf(lhs.getWeath())>Long.valueOf(rhs.getWeath())){
					return -1;
				}else if(Long.valueOf(lhs.getWeath())<Long.valueOf(rhs.getWeath())){
					return 1;	
				}else{
					return 0;
				}
			}
		}else{
			if(Long.valueOf(lhs.getWeath())>Long.valueOf(rhs.getWeath())){
				return -1;
			}else if(Long.valueOf(lhs.getWeath())<Long.valueOf(rhs.getWeath())){
				return 1;	
			}else{
				return 0;
			}
		}
	}
}
