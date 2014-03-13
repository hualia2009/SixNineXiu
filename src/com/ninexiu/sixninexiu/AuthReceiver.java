package com.ninexiu.sixninexiu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;

public class AuthReceiver extends BroadcastReceiver {
	 private static final String TAG="AuthReceiver";
	 private String access_token;
	 private Handler uiHandler;
	public AuthReceiver(Handler uiHandler) {
		this.uiHandler=uiHandler;
	}
	@Override
	public void onReceive(final Context context, Intent intent) {
	            Bundle exts = intent.getExtras();
	            access_token =  exts.getString("access_token");
	            if (access_token != null)
	            {
	            //用access token 来获取open id
	            	TencentOpenAPI.openid(access_token,new Callback() {
						@Override
						public void onSuccess(Object arg0) {
							OpenId openId=(OpenId) arg0;
							String id=openId.getOpenId();
//							SharedPreferences preferences=context.getSharedPreferences("qq_info", Context.MODE_PRIVATE);
//							Editor editor=preferences.edit();
//							editor.putString("openId", id);
//							editor.putString("access_token", access_token);
//							editor.putLong("expires", saveExpires);
//							editor.commit();
							uiHandler.sendMessage(uiHandler.obtainMessage(100, id));
						}
						
						@Override
						public void onFail(int arg0, String arg1) {
							
						}
						
						@Override
						public void onCancel(int arg0) {
							
						}
					});
	           }
		
	}

}
