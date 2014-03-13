package com.ninexiu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class SqliteHandler {
	private Context context = null;
	private SQLiteDatabase sdb = null;
	private static final String DB_NAME = "Chinacity.db"; // 保存的数据库文件名
	private  String DATABASE_PATH =null;

	public SqliteHandler(Context context) {
		this.context = context;

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			DATABASE_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/nineShow";
		}else{
			DATABASE_PATH=context.getCacheDir().getAbsolutePath()+"/nineShow";
		}
		File file=new File(DATABASE_PATH);
		if(!file.exists()) file.mkdirs();
		sdb=this.openDatabase(DATABASE_PATH+"/"+DB_NAME);
	}

	/**
	 * 打开数据库
	 */
	public void open() {
//		sdb = this.openDatabase(DUMeter.DB_PATH+PACKAGE_NAME + "/" + DB_NAME);
		
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (sdb != null) {
			sdb.close();
			sdb = null;
		}
	}

	public ArrayList<String> queryProvince() {
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = sdb.query("province_table",
				new String[] { "PROVINCE" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String str = cursor.getString(cursor.getColumnIndex("PROVINCE"));
				list.add(str);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return list;
	}

	/**
	 * 根据省份查找城市
	 * 
	 * @param provincename
	 */
	public ArrayList<String> queryTown(String provincename) {
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = sdb.query("city_table", new String[] { "DISTINCT TOWN" },
				"PROVINCE=?", new String[] { provincename }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String str = cursor.getString(cursor.getColumnIndex("TOWN"));
				list.add(str);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return list;
	}

	/**
	 * 根据城市查找区域
	 * 
	 * @param cityname
	 */
	public ArrayList<String> queryCity(String townname) {
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = sdb.query("city_table", new String[] { "CITY" },
				"TOWN=?", new String[] { townname }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String str = cursor.getString(cursor.getColumnIndex("CITY"));
				list.add(str);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return list;
	}

	/**
	 * 根据区域名查找区域代码
	 * 
	 * @param cityname
	 */
	public String queryCityCode(String cityname) {
		String str = null;
		Cursor cursor = sdb.query("city_table", new String[] { "WEATHER_ID" },
				"CITY=?", new String[] { cityname }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				str = cursor.getString(cursor.getColumnIndex("WEATHER_ID"));
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return str;
	}
	
	/**
	 * 数据库写入写出操作
	 * @param dbfile
	 * @return
	 */
	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			
			if (!(new File(dbfile).exists())) {
				InputStream is = this.context.getResources().getAssets()
						.open(DB_NAME);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
