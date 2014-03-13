package com.ninexiu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ninexiu.beans.GiftAndFace;
import com.ninexiu.beans.User;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ChargeActivity;
import com.ninexiu.sixninexiu.LoginDialogActivity;
import com.ninexiu.sixninexiu.OnlyLoginActivity;
import com.ninexiu.sixninexiu.R;

/*
 * 工具类
 */
public class Utils {

	public static  String[] concelltion;
	public static  Toast toast;
	public static final File giftDir=getGiftDir();
	public static final long expireTime=24*7*60*60*1000;//每七天更新一次
	public static final float[] credits = {
		0,50,200,500,1000,2000,4000,7500,
		13000,21000,33000,48000,68000,98000,
		148000,228000,348000,528000,778000,1108000,
		1518000,2018000,2618000,3318000,4118000,5018000,
		6018000,7118000,8368000,9868000
	};

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 * 传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap,int width,int height,Context context) { 
		width = dip2px(context,width);
		height = dip2px(context,height);

		Bitmap output = Bitmap.createBitmap(width,height, Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 

		final int color = 0xff424242; 
//		final int color2 = Color.WHITE;
		//eyan removed
		final Paint paint = new Paint();
//		final Paint paint2 = new Paint(); 
//		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		final Rect mRect = new Rect(0, 0, width,height); 

//		final Rect rect2 = new Rect(0, 0, bitmap.getWidth()-10, bitmap.getHeight()-10); 
		final RectF rectF = new RectF(mRect); 
//		final RectF rectF2 = new RectF(rect2); 
		final float roundPx = width/2; 

		paint.setAntiAlias(true); 
//		paint2.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
//		paint2.setColor(color2);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, mRect, paint); 
//		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_OVER)); 
//		canvas.drawCircle(roundPx, roundPx, roundPx+5, paint2);
		return output; 
		} 
	
	/*
	*//**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 

		final int color = 0xff424242; 
//		final int color2 = Color.WHITE;
		//eyan removed
		final Paint paint = new Paint(); 
//		final Paint paint2 = new Paint(); 
//		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 

//		final Rect rect2 = new Rect(0, 0, bitmap.getWidth()-10, bitmap.getHeight()-10); 
		final RectF rectF = new RectF(rect); 
//		final RectF rectF2 = new RectF(rect2); 
		final float roundPx = bitmap.getWidth()/2; 

		paint.setAntiAlias(true); 
//		paint2.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
//		paint2.setColor(color2);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, rect, paint); 
//		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_OVER)); 
//		canvas.drawCircle(roundPx, roundPx, roundPx+5, paint2);
		return output; 
		} 
	
	
	/***
	 * PorterDuff.Mode.CLEAR 清除画布上图像
        PorterDuff.Mode.SRC 显示上层图像
        PorterDuff.Mode.DST 显示下层图像
        PorterDuff.Mode.SRC_OVER上下层图像都显示，下层居上显示
        PorterDuff.Mode.DST_OVER 上下层都显示,下层居上显示
        PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        PorterDuff.Mode.SRC_OUT 取上层图像非交集部分
        PorterDuff.Mode.DST_OUT 取下层图像非交集部分
        PorterDuff.Mode.SRC_ATOP 取下层图像非交集部分与上层图像交集部分
        PorterDuff.Mode.DST_ATOP 取上层图像非交集部分与下层图像交集部分
        PorterDuff.Mode.XOR 取两层图像的非交集部分
	 */
	//
	/**
	 * 图片圆角处理  
	 * @param mBitmap
	 * @return
	 */
	public static  Bitmap getRoundedBitmap(Bitmap mBitmap) {  
	        //创建新的位图  
	        Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);  
	        //把创建的位图作为画板  
	        Canvas mCanvas = new Canvas(bgBitmap);  
	          
	        Paint mPaint = new Paint();  
	        Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());  
	        RectF mRectF = new RectF(mRect);  
	        //设置圆角半径为20  
	        float roundPx =8;  
	        mPaint.setAntiAlias(true);  
	        //先绘制圆角矩形  
	        mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);  
	          
	        //设置图像的叠加模式  
	        mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        //绘制图像  
	        mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);  
	          
	        return bgBitmap;  
	}  

	/***
	 * 动态设置listview的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// params.height += 5;// if without this statement,the listview will be
		// a little short listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	
	public static void MakeToast(Context context, String message) {
		if(context!=null){
			LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflate.inflate(R.layout.custom_toast, null);
			if(toast==null){
				toast = new Toast(context);
				toast.setView(view);
				toast.setText(message);
				toast.setDuration(Toast.LENGTH_SHORT);
			}else{
				toast.setView(view);
				toast.setText(message);
			}
			toast.show();
		}
		
	}

	public static int setHostLevel(String level, ImageView iv) {
		if(Utils.isNotEmptyString(level)){
		long id = Long.valueOf(level.trim());
		if(id==0){
			iv.setImageResource(R.drawable.host_level_1);
			return 1;
		}else if(0<id&&id<50000){
			iv.setImageResource(R.drawable.host_level_1);
			return 1;
		}else if(50000<=id&&id<200000){
			iv.setImageResource(R.drawable.host_level_2);
			return 2;
		}else if(50000<=id&&id<200000){
			iv.setImageResource(R.drawable.host_level_3);
			return 3;
		}else if(200000<=id&&id<1000000){
			iv.setImageResource(R.drawable.host_level_4);
			return 4;
		}else if(1000000<=id&&id<2000000){
			iv.setImageResource(R.drawable.host_level_5);
			return 5;
		}else if(2000000<=id&&id<4000000){
			iv.setImageResource(R.drawable.host_level_6);
			return 6;
		}else if(4000000<=id&&id<7500000){
			iv.setImageResource(R.drawable.host_level_7);
			return 7;
		}else if(7500000<=id&&id<13000000){
			iv.setImageResource(R.drawable.host_level_8);
			return 8;
		}else if(13000000<=id&&id<21000000){
			iv.setImageResource(R.drawable.host_level_9);
			return 9;
		}else if(21000000<=id&&id<33000000){
			iv.setImageResource(R.drawable.host_level_10);
			return 10;
		}else if(33000000<=id&&id<48000000){
			iv.setImageResource(R.drawable.host_level_11);
			return 11;
		}else if(48000000<=id&&id<68000000){
			iv.setImageResource(R.drawable.host_level_12);
			return 12;
		}else if(68000000<=id&&id<98000000){
			iv.setImageResource(R.drawable.host_level_13);
			return 13;
		}else if(98000000<=id&&id<148000000){
			iv.setImageResource(R.drawable.host_level_14);
			return 14;
		}else if(148000000<=id&&id<228000000){
			iv.setImageResource(R.drawable.host_level_15);
			return 15;
		}else if(228000000<=id&&id<348000000){
			iv.setImageResource(R.drawable.host_level_16);
			return 16;
		}else if(348000000<=id&&id<528000000){
			iv.setImageResource(R.drawable.host_level_17);
			return 17;
		}else if(528000000<=id&&id<778000000){
			iv.setImageResource(R.drawable.host_level_18);
			return 18;
		}else if(778000000<=id&&id<1108000000){
			iv.setImageResource(R.drawable.host_level_19);
			return 19;
		}else if(1108000000<=id&&id<1518000000){
			iv.setImageResource(R.drawable.host_level_20);
			return 20;
		}else if(1518000000<=id&&id<2018000000){
			iv.setImageResource(R.drawable.host_level_21);
			return 21;
		}else if(2018000000<=id&&id<Long.valueOf("2618000000")){
			iv.setImageResource(R.drawable.host_level_22);
			return 22;
		}else if(Long.valueOf("2618000000")<=id&&id<Long.valueOf("3318000000")){
			iv.setImageResource(R.drawable.host_level_23);
			return 23;
		}else if(Long.valueOf("3318000000")<=id&&id<Long.valueOf("4118000000")){
			iv.setImageResource(R.drawable.host_level_24);
			return 24;
		}else if(Long.valueOf("4118000000")<=id&&id<Long.valueOf("5018000000")){
			iv.setImageResource(R.drawable.host_level_25);
			return 25;
		}else if(Long.valueOf("5018000000")<=id&&id<Long.valueOf("6018000000")){
			iv.setImageResource(R.drawable.host_level_26);
			return 26;
		}else if(Long.valueOf("6018000000")<=id&&id<Long.valueOf("7118000000")){
			iv.setImageResource(R.drawable.host_level_27);
			return 27;
		}else if(Long.valueOf("7118000000")<=id&&id<Long.valueOf("8368000000")){
			iv.setImageResource(R.drawable.host_level_28);
			return 28;
		}else if(Long.valueOf("8368000000")<=id&&id<Long.valueOf("9868000000")){
			iv.setImageResource(R.drawable.host_level_29);
			return 29;
		}else {
			iv.setImageResource(R.drawable.host_level_30);
			return 30;
		}

		}
		return 0;

	}

	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isNotEmptyString(String input) {
		if (input!=null&&!input.equals("null")) {
			if (input.trim().length()>0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static void setUserLevel(String level, ImageView iv) {
		if (isNotEmptyString(level)) {
			Long lev =Long.valueOf(level);
			if(lev==0){
				//平民
				iv.setImageResource(R.drawable.user_level_1);
			}else if(lev>0&&lev<=2000){
				//平民
				iv.setImageResource(R.drawable.user_level_1);
			}else if(lev>2000&&lev<12000){
				//1富
				iv.setImageResource(R.drawable.user_level_2);
			}else if(lev>=12000&&lev<42000){
				//2富
				iv.setImageResource(R.drawable.user_level_3);
			}else if(lev>=42000&&lev<142000){
				//3富
				iv.setImageResource(R.drawable.user_level_4);
			}else if(lev>=142000&&lev<342000){
				
				iv.setImageResource(R.drawable.user_level_5);
			}else if(lev>=342000&&lev<742000){
				
				iv.setImageResource(R.drawable.user_level_6);
			}else if(lev>=742000&&lev<1542000){
				iv.setImageResource(R.drawable.user_level_7);
			}else if(lev>=1542000&&lev<3142000){
				iv.setImageResource(R.drawable.user_level_8);
			}else if(lev>=3142000&&lev<5742000){
				iv.setImageResource(R.drawable.user_level_9);
			}else if(lev>=5742000&&lev<9742000){
				iv.setImageResource(R.drawable.user_level_10);
			}else if(lev>=9742000&&lev<16242000){
				iv.setImageResource(R.drawable.user_level_11);
			}else if(lev>=16242000&&lev<26242000){
				iv.setImageResource(R.drawable.user_level_12);
			}else if(lev>=26242000&&lev<41242000){
				iv.setImageResource(R.drawable.user_level_13);
			}else if(lev>=41242000&&lev<61242000){
				iv.setImageResource(R.drawable.user_level_14);
			}else if(lev>=61242000&&lev<86242000){
				iv.setImageResource(R.drawable.user_level_15);
			}else if(lev>=86242000&&lev<121242000){
				iv.setImageResource(R.drawable.user_level_16);
			}else if(lev>=121242000&&lev<171242000){
				iv.setImageResource(R.drawable.user_level_17);
			}else if(lev>=171242000&&lev<251242000){
				iv.setImageResource(R.drawable.user_level_18);
			}else if(lev>=251242000&&lev<271242000){
				iv.setImageResource(R.drawable.user_level_19);
			}else if(lev>=271242000&&lev<441242000){
				iv.setImageResource(R.drawable.user_level_20);
			}else if(lev>=441242000&&lev<671242000){
				iv.setImageResource(R.drawable.user_level_21);
			}else if(lev>=671242000&&lev<971242000){
				iv.setImageResource(R.drawable.user_level_22);
			}else if(lev>=971242000&&lev<1351242000){
				iv.setImageResource(R.drawable.user_level_23);
			}else if(lev>=1351242000&&lev<1821242000){
				iv.setImageResource(R.drawable.user_level_24);
			}else if(lev>=1821242000&&lev<Long.valueOf("2391242000")){
				iv.setImageResource(R.drawable.user_level_25);
			}else if(lev>=Long.valueOf("2391242000")&&lev<Long.valueOf("3071242000")){
				iv.setImageResource(R.drawable.user_level_26);
			}else if(lev>=Long.valueOf("3071242000")&&lev<Long.valueOf("4051242000")){
				iv.setImageResource(R.drawable.user_level_27);
			}else if(lev>=Long.valueOf("4051242000")&&lev<Long.valueOf("5331242000")){
				iv.setImageResource(R.drawable.user_level_28);
			}else if(lev>=Long.valueOf("5331242000")&&lev<Long.valueOf("7891242000")){
				iv.setImageResource(R.drawable.user_level_29);
			}else if(lev>=Long.valueOf("userWeath>7891242000")){
				iv.setImageResource(R.drawable.user_level_30);
			}
		}

		}
	
	
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 */
    public static void dealExpression(Context context,SpannableStringBuilder spannableString, Pattern patten) throws Exception {
    	Matcher matcher = patten.matcher(spannableString);
//    	DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
//    	float dest=displayMetrics.scaledDensity;
        while (matcher.find()) {
            String key = matcher.group();
            Drawable drawable=queryGiftAndFaceByTag(key,context);
            if(drawable!=null){
                ImageSpan imageSpan = new ImageSpan(drawable,DynamicDrawableSpan.ALIGN_BOTTOM);	
                drawable.setBounds(10, 10, drawable.getIntrinsicWidth()+10, drawable.getIntrinsicHeight()+10);
//                 drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*dest), (int)(drawable.getIntrinsicHeight()*dest));
                int end = matcher.start() + key.length();					
                spannableString.setSpan(imageSpan, matcher.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);	
            }
        }
    }
    
    public static SpannableStringBuilder getExpressionString(Context context,String str,String zhengze){
    	SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);//通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context,spannableString, sinaPatten);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }
    
    /*
     * 查找本地缓存图片的目录 
     */
    public static File getCachDir(){
    	File cachDir=null;
    		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    			cachDir=new File(Environment.getExternalStorageDirectory(),"nineShow/zip/");
    			if(!cachDir.exists()){
    				cachDir.mkdirs();
    			}
    			return cachDir;
    		}else{
    	    	return null;
    		}
    	}
    
    
    /*
     * 保存礼物小图片的地址
     */
    public static File getGiftDir(){
    	File cacheDir=null;
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			cacheDir=new File(Environment.getExternalStorageDirectory(),"nineShow/.gift/");
				if(!cacheDir.exists()){
					cacheDir.mkdirs();
				}
		}
    	return cacheDir;
    }
    
    public static File getImageCacheDir(Context mContext){
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    		return new File(Environment.getExternalStorageDirectory(),"nineShow/.cache/");
    	else
    		return new File(mContext.getCacheDir(),".cache");
    }
    
    /**
     * 根据表情icon查找表情
     * @param iconStr
     * @param context 
     * @param dest 
     * @param faces
     * @return
     */
    public  static Drawable queryGiftAndFaceByTag(String iconStr, Context context){
    	Drawable drawable=null;
    	if(iconStr.indexOf("imgface")!=-1){
    		String array[]=iconStr.split("imgface");
        	int index=array[1].indexOf("#");
        	String id=array[1].substring(0,index);
        	 drawable=context.getResources().getDrawable(AppConstants.EMOTION_ID[Integer.valueOf(id)-1]);
        	if(drawable!=null){
        		return drawable;
        	}else{
        		return null;
        	}
    	}
    	
    	//[#mgift10#]
    	if(iconStr.indexOf("mgift")!=-1){
    		if(giftDir!=null){
    			String array[]=iconStr.split("mgift");
            	int index=array[1].indexOf("#");
            	String id=array[1].substring(0,index);
            	if(id.equals("999")){
            		drawable=context.getResources().getDrawable(R.drawable.yuanbao);
            		return drawable;
            	}else{
            		File image=new File(giftDir, "mgift"+id+".png");
    				if(image.exists()){
    					 drawable=Drawable.createFromPath(image.getAbsolutePath());
    					 return drawable;
    				}
            	}
    	}
    	}
    	
    	//座驾
    	if(iconStr.indexOf("car")!=-1){
    		String array[]=iconStr.split("car");
        	int index=array[1].indexOf("#");
        	String id=array[1].substring(0,index);
        	File image=new File(giftDir, "car"+id+".png");
			if(image.exists()){
				 drawable=Drawable.createFromPath(image.getAbsolutePath());
				 return drawable;
			}
        	
    	}
    	//彩条
    	if(iconStr.indexOf("colorbar")!=-1){
    		//[#colorbar1#]
    		String array[]=iconStr.split("colorbar");
        	int index=array[1].indexOf("#");
        	String id=array[1].substring(0,index);
        	drawable=context.getResources().getDrawable(AppConstants.COLOR_BAR[Integer.valueOf(id)-1]);
        	if(drawable!=null){
        		  return drawable;
        	}else{
        		return null;
        	}
    	}
    	return null;
    }
    
    
    
    /*
     * 下载除了豪华礼物之外的其他礼物文件
     */
    public static void downloadFile(final File dir,final String url,boolean needUpdate){
    	if(dir!=null){
    	final String fileName=url.substring(url.lastIndexOf("/")+1, url.length());
		final File targetFile=new File(dir,fileName);
		if(targetFile.length()==0||(System.currentTimeMillis()-targetFile.lastModified()>expireTime)||needUpdate){
			try {
				if(!targetFile.exists())
				targetFile.createNewFile();
		    	new Thread(new Runnable() {
		    			InputStream inputStream;
		    			FileOutputStream fileOutputStream;
					@Override
					public void run() {  //api4.0以上下载文件必须开启线程 否则报异常 （这错误搞了好久才发现 艹）
						try {
							URL uri=new URL(url);
							HttpURLConnection urlConnection=(HttpURLConnection) uri.openConnection();
							urlConnection.setConnectTimeout(700000);
							urlConnection.setReadTimeout(50000);
							if(urlConnection.getResponseCode()==200){
								 inputStream=urlConnection.getInputStream();
									fileOutputStream=new FileOutputStream(targetFile);
									byte[] buffer=new byte[1024];
									int len;
									while((len=inputStream.read(buffer))!=-1){
										fileOutputStream.write(buffer, 0, len);
									}
									if(url.contains("edge_includes.zip")){
										try {
											Utils.unzip(dir.getAbsolutePath()+File.separator+fileName, dir.getAbsolutePath()+File.separator+"html");
										} catch (Exception e) {
											e.printStackTrace();
											Utils.unzip(dir.getAbsolutePath()+File.separator+fileName, dir.getAbsolutePath()+File.separator+"html");
										}
										
									}else if(url.contains(".zip")){
										try {
											Utils.unzip(targetFile.getAbsolutePath(), dir.getAbsoluteFile()+File.separator+"html");
										} catch (Exception e) {
											e.printStackTrace();
											Utils.unzip(targetFile.getAbsolutePath(), dir.getAbsoluteFile()+File.separator+"html");
										}
									}
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
				    	finally{
				    		if(inputStream!=null){
				    			try {
									inputStream.close();
									inputStream=null;
								} catch (Exception e) {
									e.printStackTrace();
								}
				    		}
				    		if(fileOutputStream!=null){
				    			try {
									fileOutputStream.close();
									fileOutputStream=null;
								} catch (Exception e) {
									e.printStackTrace();
								}
				    		}
				    	}
						
					}
				}).start();
		    	
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	    }else{
	    	return;
	    }
		}
    }
    
    
    /**
     * 根据礼物名字 查找礼物
     * @param giftName
     * @return
     */
    public static String queryGiftByName(String giftName){
    	File giftDir=getCachDir();
    	if(giftDir!=null){
    		File[] cacheFiles = giftDir.listFiles();    
            int i = 0;    
            if(null!=cacheFiles){  
            for(; i<cacheFiles.length; i++)    
            {    
                if(giftName.equals(cacheFiles[i].getName()))    
                {    
                    break;    
                }    
            }    
                
            if(i < cacheFiles.length)    
            {    
            	File file=new File(giftDir.getAbsolutePath(),giftName);
                return file.getPath();    
            } 
    	}
    }
    	return null;
    }
    
    public static GiftAndFace queryGiftAndFaceById(String id,ArrayList<GiftAndFace> datas){
    
    	for (int i = 0; i < datas.size(); i++) {
			GiftAndFace face=datas.get(i);
			if(face.getGiftId().equals(id)){
				return face;
			}
		}
    	return null;
    }
    
    
	
	/* 
     * 这个是解压ZIP格式文件的方法 
     *  
     * @zipFileName：是传进来你要解压的文件路径，包括文件的名字； 
     *  
     * @outputDirectory:选择你要保存的路劲； 
     *  
     */  
    public static void unzip(String zipFileName,String outputDirectory) throws Exception{
					ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));  
			        ZipEntry z;  
			        String name = "";  
			        String extractedFile = "";  
			        int counter = 0;  
			        File dir=new File(outputDirectory);
			        if(!dir.exists()){
			        	dir.mkdir();
			        }
			        while ((z = in.getNextEntry()) != null) {  
			            name = z.getName();  
			            Log.v("UtilsZip", "unzipping file: " + name);  
			            if (z.isDirectory()) {  
//			                Log.e("UtilsZip", name + "is a folder");  
			                // get the folder name of the widget  
			                name = name.substring(0, name.length()-1);  
			                File folder = new File(outputDirectory + File.separator + name);
			                if(!folder.exists()){
			                	 folder.mkdirs();  
			                }
			                if (counter == 0) {  
			                    extractedFile = folder.toString();  
			                }  
			                counter++;  
//			                Log.e("UtilsZip", "mkdir " + outputDirectory + File.separator + name);  
			            } else {  
//			                Log.e("UtilsZip", name + " is a normal file");  
			                File file = new File(outputDirectory + File.separator + name);
			                if(!file.exists()&&file.length()==0){
			                file.createNewFile();  
			                // get the output stream of the file  
			                FileOutputStream out = new FileOutputStream(file);  
			                int ch;  
			                byte[] buffer = new byte[1024];  
			                // read (ch) bytes into buffer  
			                while ((ch = in.read(buffer))!= -1) {  
			                    // write (ch) byte from buffer at the position 0  
			                    out.write(buffer, 0, ch);  
			                }  
			                out.flush();  
			                out.close();
			            }  
			            }
			        }  
			        in.close();  
    }  
    
    
  
     
    
 // 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 设置标签
     * @param tag
     * @return
     */
    public static ArrayList<String> getTag(String tag){
    	ArrayList<String> tags=new ArrayList<String>();
    	tags.add(tag);
    	return tags;
    }
    
    
    public static String stringFilter(String str) {         
        str = str.replaceAll("【", "[").replaceAll("】", "]")
               .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符         
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
   }  
    
    /**
     * 生成对话框
     * @param context
     * @return
     */
    public static Dialog showProgressDialog(Activity context,String message){
		Dialog dialog=new Dialog(context, R.style.loading_dialog);
		dialog.setCanceledOnTouchOutside(true);
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		dialog.setContentView(view);
		TextView tv=(TextView) view.findViewById(R.id.tipTextView);
		tv.setText(message);
		ImageView imageView=(ImageView) view.findViewById(R.id.img);
		imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_animation));
		return dialog;
    }
    
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) { 
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }      
    
    public static void OrderSong(final Activity context,String songName,User mUser,String roomId,String toMsg){
    	if(mUser!=null){
    		final Dialog dialog=showProgressDialog(context, "操作中...");
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			params.put("rid", roomId);
			params.put("musicname", songName);
			params.put("tomsg", toMsg);
			asyncHttpClient.post(AppConstants.ORDER_SONG, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
					dialog.show();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
						dialog.dismiss();
						try {
							JSONObject jsonObject=new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success==true){
								Utils.MakeToast(context, "点歌请求已发送");
							}else{
								String msg=jsonObject.optString("msg");
								Utils.MakeToast(context, msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						//Log.e("OrderSong", content);
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					dialog.dismiss();
					Utils.MakeToast(context, "网络请求超时");
				}
			});
		}else{
			Intent intent=new Intent(context,OnlyLoginActivity.class);
			intent.putExtra("fromChatRoom", true);
			context.startActivity(intent);
		}
    }
    
    
    public static  void showToSomeDialog(final Activity context,final String songName,final User mUser,final String roomId){
    	AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("告白");
		builder.setMessage("将这首歌送给");
		final EditText view=new EditText(context);
		builder.setView(view);
		builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String msg=view.getText().toString().trim();
				if(msg.length()>0){
					OrderSong(context, songName, mUser, roomId,msg);
				}else{
					OrderSong(context, songName, mUser, roomId,"");
				}
			}
		});
		builder.setNegativeButton("跳过", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Utils.OrderSong(context, songName, mUser, roomId,"");
			}
		});
		builder.show();
    }
    
    
    
    
    public static boolean canRegOrLogin(Context contxt,String userName,String pwd){
    	if(userName.length()==0){
			Utils.MakeToast(contxt, "用户名不能为空");
			return false;
		}
    	
    	if(!Character.isLetter(userName.charAt(0))){
    		Utils.MakeToast(contxt, "用户名必须以字母开头");
    		return false;
    	}
    	
		if(userName.length()<4){
			Utils.MakeToast(contxt, "用户名必须四位以上");
			return false;
		}
		if(userName.length()>16){
			Utils.MakeToast(contxt, "用户名必须十六位以下");
			return false;
		}
		if(pwd.length()==0){
			Utils.MakeToast(contxt, "密码不能为空");
			return false;
		}
		if(pwd.length()<6){
			Utils.MakeToast(contxt, "密码必须六位以上");
			return false ;
		}
		if(pwd.length()>16){
			Utils.MakeToast(contxt, "密码必须十六位以下");
			return false;
		}
		return true;
    }
    
    
    /**  
     * 验证手机号码  
     * @param mobiles  
     * @return  [0-9]{5,9}  
     */  
    public static boolean isMobileNO(String mobiles){  
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	Matcher m = p.matcher(mobiles);
    	return m.matches();
    } 
    
    /**
     * 判断用户是否登录过 
     * @param context
     * @return
     */
    /*public static User isUserLogin(Context context){
    	SharedPreferences preferences=context.getSharedPreferences(AppConstants.LOGIN_STATUS, Context.MODE_PRIVATE);
    	Log.v("IsLogin", preferences.getBoolean(AppConstants.IS_LOGIN,false)+"");
    	if(preferences.getBoolean(AppConstants.IS_LOGIN, false)){
    		ObjectInputStream objectIputStream = null;
        	User mUser;
    		try {
    			objectIputStream = new ObjectInputStream(context.openFileInput(AppConstants.USER_INFO));
    			mUser=(User) objectIputStream.readObject();
    			return mUser;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}finally{
    			if(objectIputStream!=null){
    				try {
    					objectIputStream.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    	return null;
    }*/
    
   /* *//**
     * 得到图片下载的工具
     * @param context
     * @return
     *//*
    public static FinalBitmap getBitmapLoad(Context context,boolean isHall){
    	FinalBitmap bitmap=FinalBitmap.create(context);
//    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//    		File imageCache=new File(Environment.getExternalStorageDirectory(),"nineShow/ImageCache/");
//			if(!imageCache.exists()){
//				imageCache.mkdirs();
//			}
////			 bitmap=FinalBitmap.create(context, imageCache.getAbsolutePath(), (float)0.5, 2);
//			 bitmap=FinalBitmap.create(context);
//    	}else{
//    		File imageCache=new File(context.getCacheDir(),"ImageCache/");
//    		if(!imageCache.exists()){
//				imageCache.mkdirs();
//			}
//			bitmap=FinalBitmap.create(context, imageCache.getAbsolutePath(), (float)0.5, 2);
//    	}
    	if(isHall){
    		bitmap.configLoadfailImage(R.drawable.loading_image);
    		bitmap.configLoadingImage(R.drawable.loading_image);
    	}else{
    		bitmap.configLoadfailImage(R.drawable.loading_smale);
    		bitmap.configLoadingImage(R.drawable.loading_smale);
    	}
    	return bitmap;
    }*/
    
    public static  File getLevelJsonFile(Context context,String fileName){
    	File level=null;
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		level=new File(Environment.getExternalStorageDirectory(),"nineShow/"+fileName);
    		if(!level.exists()){
    			try {
					level.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}else{
    		 level=new File(context.getCacheDir(),"nineShow/"+fileName);
    		 if(!level.exists()){
    			 try {
					level.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		 }
    	}
    	return level;
    }
    
	public static String getLineString(InputStream in){
		StringBuffer sb = new StringBuffer();
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String temp;
		try {
			while ((temp=read.readLine())!=null) {
				sb.append(temp);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
    	
	/**
	 * 重新保存用户信息
	 * @param muser
	 */
	/*public static void saveUser(Context context,User mUser){
		//本地缓存用户信息
		ObjectOutputStream objectOutputStream=null;
		try {
			 objectOutputStream=new ObjectOutputStream(context.openFileOutput(AppConstants.USER_INFO, Context.MODE_PRIVATE));
			 objectOutputStream.writeObject(mUser);
			 objectOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(objectOutputStream!=null)
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}*/
	
	
	/**
	 * 从服务器获取用户最新的信息 保存
	 * @param mUser
	 * @param mContext
	 */
	public static void UpdateUserInfo(final User mUser,final Context mContext){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_USER_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
    					JSONObject jsonObject=new JSONObject(content);
    					boolean isSuccess=jsonObject.getBoolean("success");
    					if(isSuccess==true){
    						JSONObject jsonObject2=jsonObject.getJSONObject("retval");
    						if(jsonObject2!=null){
    							ApplicationEx.get().getUserManager().parseJSONUser(jsonObject2);
    						}
    					}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
					
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}
	
	/*public static final User parseJSONUser(JSONObject jsonObject2){
		LogUtil.writeDataToFile("parserUser:", jsonObject2.toString());
		User user=new User();
		user.setNickName(jsonObject2.optString("nickname"));
		user.setToken(jsonObject2.optString("token"));
		user.setUid(jsonObject2.optString("uid"));
		user.setAvatar(jsonObject2.optString("avatar"));
		user.setVipType(jsonObject2.optString("viptype"));
		user.setUserCoin(jsonObject2.optInt("usercoins"));
		user.setUserDian(jsonObject2.optInt("userdian"));
		LogUtil.writeDataToFile("九币九点", String.format("九币：%s，九点：%s", user.getUserCoin(), user.getUserDian()));
//		VolleyLog.e("九币：%s，九点：%s", user.getUserCoin(), user.getUserDian());
		user.setSex(jsonObject2.optString("sex"));
		user.setProvice(jsonObject2.optString("province"));
		user.setCity(jsonObject2.optString("city"));
		user.setWeath(jsonObject2.optString("wealth"));
		user.setCredit(jsonObject2.optString("credit"));
		user.setUserNum(jsonObject2.optString("usernum"));
		user.setUserType(jsonObject2.optString("usertype"));
		//Log.e("test","data="+jsonObject2.toString());
		user.setUser_props(jsonObject2.optString("user_props_id"));
		JSONObject noble=jsonObject2.optJSONObject("topAristocrat");
		if(noble!=null){
			user.setNobel(true);
		}
		user.setUserDetail(jsonObject2.toString());
		return user;
	}*/
	/**
	 * 判断用户是否是VIP 用户
	 * @param value
	 * @return
	 */
	public static boolean isVip(String value){
		if(Utils.isNotEmptyString(value)){
			int vip=Integer.valueOf(value);
			if(vip==800001||vip==800002||vip==800003){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 执行新手任务
	 * @param mUser
	 * @param uid
	 */
	public static void doUserTask(User mUser,final String uid,final Handler uiHandler){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("taskId", uid);
		asyncHttpClient.post(AppConstants.FINSIH_TASK, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(uiHandler!=null){
					uiHandler.sendMessage(uiHandler.obtainMessage(100, uid));	
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
	}
	
	public static void showLoginDialog(final Context context,boolean fromChatRoom){
//    		AlertDialog.Builder builder=new AlertDialog.Builder(context);
//			builder.setTitle("提示消息");
//			builder.setIcon(android.R.drawable.ic_dialog_info);
//			builder.setMessage("此功能需要您登录之后才能使用");
//			builder.setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					//未登录则要求用户登录
//					Intent intent=new Intent(context,LoginActivity.class);
//					context.startActivity(intent);
//				}
//			});
//			builder.setNegativeButton("稍后登录", null);
//			builder.show();
		Intent intent=new Intent(context,LoginDialogActivity.class);
		intent.putExtra("fromChatRoom", fromChatRoom);
		context.startActivity(intent);
	}
	
	
	public static void showChargeDialog(final Context mContext){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		builder.setTitle("充值");
		builder.setMessage("九币不足,是否充值?");
		builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent(mContext,ChargeActivity.class);
				mContext.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	
	public static boolean isMobileCardOk(String cardType,String cardNumber,String cardPwd){
		//移动充值卡类型判断
		if(cardType.equals("SZX")){
			if(cardNumber.length()==17||cardPwd.length()==18){
				if(isDigital(cardPwd)){
    				return true;
    			}else{
    				return false;
    			}
			}
			
			if(cardNumber.length()==10||cardPwd.length()==8){
				return true;
			}
			
			if(cardNumber.length()==16||cardPwd.length()==17){
				return true;
			}
			
			return false;
		}
		
		if(cardType.equals("UNICOM")){
			if(cardNumber.length()==15||cardPwd.length()==19){
				if(isDigital(cardNumber)&&isDigital(cardPwd)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		
		if(cardType.equals("TELECOM")){
			if(cardNumber.length()==19||cardPwd.length()==18){
				if(isDigital(cardPwd)){
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public static boolean isDigital(String number){
		char[] ar=number.toCharArray();
		for (int i = 0; i < ar.length; i++) {
			if(!Character.isDigit(ar[i])){
				return false;
			}
		}
		return true;
	}    
	
	public static int getCredit(long credit){
		int size = credits.length;
		for(int i = 0;i < size;i++){
			if(credit <= credits[i]*1000){
				return i+1;
			}
		}
		return 1;
	}
	
	//public static Context mContext;
	public static boolean isNetwokAvailable(Context context){
	//	if(context == null)
	//		context = mContext;
		if(context != null){
			    ConnectivityManager connMgr  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
			    NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
    	        NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
    	        if(wifiInfo.isAvailable()){  
    	            return true;  
    	        }
    	        if(mobileInfo.isAvailable()){  
    	            return true;  
    	        }
    	}    	
		return false;    	     
	}
	
	/**
	 * 删除ArrayList中重复元素，保持顺序
	 * @param list
	 * @return
	 */
	public static ArrayList removeDuplicateWithOrder(ArrayList list) {

		   Set set = new HashSet();

		   ArrayList newList = new ArrayList();

		   for (Iterator iter = list.iterator(); iter.hasNext();) {

		          Object element = iter.next();

		          if (set.add(element))

		             newList.add(element);

		       } 

		      list.clear();

		      list.addAll(newList);

		      return list;
		}
}
