package com.ninexiu.sixninexiu;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.util.Log;

public class DefaultExceptionHandler implements UncaughtExceptionHandler {
	
	public DefaultExceptionHandler(Activity rankActivity) {
		// TODO Auto-generated constructor stub
		Log.e("test","DefaultExceptionHandler...");
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		Log.e("test","uncaughtException...");
		Log.w("test",arg1);
		// TODO Auto-generated method stub
		//FileUtil.logAppendFile(arg1.getCause().toString());
		//FileUtil.saveAppendFile();
	}

}
