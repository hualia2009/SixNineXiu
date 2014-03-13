package com.ninexiu.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.ninexiu.beans.User;

/**
 * 用户管理类
 */
public class UserManager {
	private User mUser;
	private List<IUserManager> mUserManagers = new ArrayList<IUserManager>();
	/** 用户信息改变，进行通知 */
	private List<IUserDataChanged> mUserDataChanged = new ArrayList<IUserDataChanged>();
	
	public void setUser(User mUser) {
		this.mUser = mUser;
	}

	public User getUser() {
		return mUser;
	}

	public void add(IUserManager iUserManager) {
		mUserManagers.add(iUserManager);
	}

	public void remove(IUserManager iUserManager) {
		mUserManagers.remove(iUserManager);
	}

	public void notifyState() {
		for (int i = 0; i < mUserManagers.size(); i++) {
			mUserManagers.get(i).IUserStateChanged();
		}
	}

	public void addUserDataChangedListener(IUserDataChanged iUserDataChanged) {
		mUserDataChanged.add(iUserDataChanged);
	}

	public void removeUserDataChangedListener(IUserDataChanged iUserDataChanged) {
		mUserDataChanged.remove(iUserDataChanged);
	}

	public void notifyUserDataChanged(User user) {
		for (int i = 0; i < mUserDataChanged.size(); i++) {
			mUserDataChanged.get(i).IUserStateChanged(user);
		}
	}
	
	public void saveUserData(String userName, String pwd, boolean login) {
		MoccaPreferences.LOGGED.put(login);
		MoccaPreferences.USERNAME.put(userName);
		MoccaPreferences.USERPWD.put(pwd);
	}

	public final synchronized User parseJSONUser(JSONObject jsonObject2) {
		if (null == mUser) {
			mUser = new User();
		}
		mUser.setNickName(jsonObject2.optString("nickname"));
		mUser.setToken(jsonObject2.optString("token"));
		mUser.setUid(jsonObject2.optString("uid"));
		mUser.setAvatar(jsonObject2.optString("avatar"));
		mUser.setVipType(jsonObject2.optString("viptype"));
		mUser.setUserCoin(jsonObject2.optInt("usercoins"));
		mUser.setUserDian(jsonObject2.optInt("userdian"));
		// VolleyLog.e("九币：%s，九点：%s", user.getUserCoin(), user.getUserDian());
		mUser.setSex(jsonObject2.optString("sex"));
		mUser.setProvice(jsonObject2.optString("province"));
		mUser.setCity(jsonObject2.optString("city"));
		mUser.setWeath(jsonObject2.optString("wealth"));
		mUser.setCredit(jsonObject2.optString("credit"));
		mUser.setUserNum(jsonObject2.optString("usernum"));
		mUser.setUserType(jsonObject2.optString("usertype"));
		// Log.e("test","data="+jsonObject2.toString());
		mUser.setUser_props(jsonObject2.optString("user_props_id"));
		JSONObject noble = jsonObject2.optJSONObject("topAristocrat");
		if (noble != null) {
			mUser.setNobel(true);
		}
		mUser.setUserDetail(jsonObject2.toString());
		notifyUserDataChanged(mUser);
		return mUser;
	}

}
