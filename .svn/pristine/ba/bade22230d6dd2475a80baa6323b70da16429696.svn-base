package com.ninexiu.utils;

import android.content.Context;

public class MoccaPreferences {

	private static final PreferenceFile sPrefs = new PreferenceFile("mocca", Context.MODE_PRIVATE);

	/** 人气主播数据 */
	public static final PreferenceFile.SharedPreference<String> POPANCHORDATA = sPrefs.value(
			"pop_anchor_data", "");

	/** 新秀主播数据 */
	public static final PreferenceFile.SharedPreference<String> RECOMMENDANCHORDATA = sPrefs.value(
			"recommend_anchor_data", "");
	
	/** 最新主播数据 */
	public static final PreferenceFile.SharedPreference<String> NEWANCHORDATA = sPrefs.value(
			"new_anchor_data", "");
	
	/** 最新主播数据 */
	public static final PreferenceFile.SharedPreference<String> HOMEADSDATA = sPrefs.value(
			"home_ads", "");
	/** 用户名 */
	public static final PreferenceFile.SharedPreference<String> USERNAME = sPrefs.value(
			"username", "");
	/** 密码 */
	public static final PreferenceFile.SharedPreference<String> USERPWD = sPrefs.value(
			"userpwd", "");
	
	/** 登录状态，用于判断是否需要自动登录 */
	public static final PreferenceFile.SharedPreference<Boolean> LOGGED = sPrefs.value(
			"is_logged", false);
	
	/** 登录类型 */
	public static final PreferenceFile.SharedPreference<Integer> LOGINTYPE = sPrefs.value(
			"login_type", 0);
	/** QQ的openid，新浪的accesstoken */
	public static final PreferenceFile.SharedPreference<String> OPENID = sPrefs.value(
			"openid", "");
	public static final PreferenceFile.SharedPreference<Integer> ISFIRSTSTART = sPrefs.value(
			"is_first_start", 0);
	
	public static PreferenceFile getPreferencesFile() {
		return sPrefs;
	}
}
