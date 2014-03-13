package com.ninexiu.utils;

import java.util.Comparator;

import com.ninexiu.beans.Father;

public class UserComparator implements Comparator<Father> {
	//1主播 ,3.运营,2.巡官 ,0 普通用户
	//VIP 钻石VIP:800003,白金VIP:800002,黄金VIP:800001
	@Override
	public int compare(Father lhs, Father rhs) {
		//首先判断用户类型
		int lhsUserType=Integer.valueOf(lhs.getUserType());
		int rhsUserType=Integer.valueOf(rhs.getUserType());
		if(lhsUserType==1&&rhsUserType!=1){
			return 1;
		}else if(lhsUserType!=1&&rhsUserType==1){
			return -1;
		}else if(lhsUserType==3&&rhsUserType!=3){
			return 1;
		}else  if(lhsUserType!=3&&rhsUserType==3){
			return -1;
		}else if(lhsUserType==2&&rhsUserType!=2){
			return 1;
		}else if(lhsUserType!=2&&rhsUserType==2){
			return -1;
		}else if(lhsUserType==0&&rhsUserType==0){
			Integer vip1=Integer.valueOf(lhs.getVipType());
			Integer vip2=Integer.valueOf(rhs.getVipType());
			if(vip1>vip2){
				return 1;
			}else if(vip1<vip2){
				return -1;
			}else if(vip1==vip2){
				Integer wealt1=Integer.valueOf(lhs.getWeath());
				Integer wealth2=Integer.valueOf(rhs.getWeath());
				if(wealt1>wealth2){
					return 1;
				}else{
					return -1;
				}
			}
		}
		return 0;
	}

}
