package com.ninexiu.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * 类描述：FileUtil
 *  @author hexiaoming
 *  @version  
 */
public class FileUtil {
	
	public static File updateDir = null;
	public static File updateFile = null;
	/***********保存升级APK的目录***********/
	public static final String NineShowApplication = "NineShowUpdateDir";
	
	public static boolean isCreateFileSucess;

	public static String getUpdateFilePath(String fileName) {
		// 当前挂载了sdcard
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			File updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + NineShowApplication +"/");
			File updateFile = new File(updateDir + "/" + fileName + ".apk");
			return updateFile.getAbsolutePath();
		}else{
			return "";
		}
	}
	
	/** 
	* 方法描述：createFile方法
	* @param   String app_name
	* @return 
	* @see FileUtil
	*/
	public static void createFile(String app_name) {
		
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			isCreateFileSucess = true;
			
			updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + NineShowApplication +"/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
	
	public static BufferedWriter writer;
	
	public static void logAppendFile(String text){
	//	Log.e("test","text="+text);
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
		 	try {
				if(writer==null){
					String path="log/log_123.txt";
					File file = new File(Environment.getExternalStorageDirectory()+ "/" + path);
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					writer = new BufferedWriter(new FileWriter(file));
				}
				writer.write(text+"\n");
				if(writer!=null)
					writer.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			Log.e("logFile","file not exist");
		}
	}
	
	public static void saveAppendFile(){
		try {
			if(writer!=null)
				writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logFile(String path,String text){
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
		 	try {
				File file = new File(Environment.getExternalStorageDirectory()+ "/" + path);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(text.getBytes("utf-8"));
				fos.flush();
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Log.e("logFile","file not exist");
		}
	}
	

	public String restoreCacheData(Activity act,String key,String content,String url){
		File file =new File(act.getFilesDir(),key);
		boolean isValid = false;
		if(file.exists() && System.currentTimeMillis()-file.lastModified()<1000*60*60*24*3)
			isValid = true;
		else if(file.exists() && !Utils.isNetwokAvailable(act))
			isValid = true;
		else if(Utils.isNetwokAvailable(act)){
			
		}
		if(content==null){
			if(isValid){
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String data="";
					String line = null;
					while(( line = br.readLine())!=null){
						data+=line;
					}
					br.close();
					return data;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			return null;
		}else{
			try {
				FileOutputStream fileOutputStream=act.openFileOutput(key, act.MODE_PRIVATE);
				fileOutputStream.write(content.getBytes());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return null;
		}
	}
	
}