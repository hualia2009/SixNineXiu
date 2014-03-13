package com.ninexiu.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();;
	private ExecutorService executorService;
//	private File cacheDir;
	private BitmapFactory.Options opt;
	private static AsyncImageLoader loader;
	
	public AsyncImageLoader() {
			/**
			 * 为了优化位图占用的内存,避免内存溢出
			 */
		 	opt= new BitmapFactory.Options();  
			opt.inPreferredConfig = Bitmap.Config.RGB_565;   
			opt.inPurgeable = true;  
			opt.inInputShareable = true; 
			executorService = Executors.newFixedThreadPool(5);
		    //cacheDir=Utils.getGiftDir();
	}

	public Bitmap loadBitmap(final String imageUrl,final ImageCallback imageCallback) {
		//eyan 12月04日 09:33 version
		/*if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}*/
		/*else{
			 /**   
             * 加上一个对本地缓存的查找   
             */    
			/*if(cacheDir!=null){
            String bitmapName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);    
            File[] cacheFiles = cacheDir.listFiles();    
            int i = 0;    
            if(null!=cacheFiles){  
            for(; i<cacheFiles.length; i++)    
            {    
                if(bitmapName.equals(cacheFiles[i].getName()))    
                {    
                    break;    
                }    
            }    
                
            if(i < cacheFiles.length)    
            {    
            	File file=new File(cacheDir.getAbsolutePath(),bitmapName);
            	if(file.exists()&&file.length()>0)
                return BitmapFactory.decodeFile(file.getPath(),opt);    
            }  
            }  
        }
		}*/
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Bitmap drawable = loadImageFromUrl(imageUrl);
				if (drawable != null) {
					//eyan 12月04日 10:46 version
					//imageCache.put(imageUrl,new SoftReference<Bitmap>(drawable));
					if(imageCallback!=null){
						Message message = handler.obtainMessage(0, drawable);
						handler.sendMessage(message);
					}
				}
			}
		});
		return null;
	}

	public Bitmap loadImageFromUrl(String url) {
		URL m;
		Bitmap d = null;
		InputStream inputStream = null;
//		 File bitmapFile = null;
		try {
			m = new URL(url);
			HttpURLConnection connection=(HttpURLConnection) m.openConnection();
			connection.setConnectTimeout(70000);
			if(connection.getResponseCode()==200){
			inputStream = connection.getInputStream();
			//缓存到本地
			/*if(cacheDir!=null){    
			File[] fiels=cacheDir.listFiles();//超过200张图片删除
			if(fiels.length>200){
				for (int j = 0; j < fiels.length; j++) {
					fiels[j].delete();
				}
			}
			bitmapFile = new File(cacheDir,url.substring(url.lastIndexOf("/") + 1));    
             if(!bitmapFile.exists())    
             {    
                 try    
                 {    
                     bitmapFile.createNewFile();    
                 }    
                 catch (IOException e)    
                 {    
                     e.printStackTrace();    
                 }    
             }    
			byte[] buffer=getByte(inputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(bitmapFile);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            fileOutputStream.close();
			}*/
//			if(bitmapFile!=null){
//				d = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(),opt);
//			}else{
			//eyan 2013 12 04
				d=BitmapFactory.decodeStream(inputStream,null,opt);
//			}
				/*BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				             
				opts.inSampleSize = computeSampleSize(opts, -1, 128*128);       
				opts.inJustDecodeBounds = false;
				d=BitmapFactory.decodeStream(inputStream,null,opt);*/

				/*try {
					Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
				    imageView.setImageBitmap(bmp);
				} catch (OutOfMemoryError err) {
					
				}	*/
				return d;
		}
			} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return d;
	}
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);
	 
	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	 
	    return roundedSize;
	}
	 
	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;
	 
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));
	 
	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }
	 
	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}   
	
	/**
	 * 输入流转换成字节数组
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private byte[] getByte(InputStream inputStream) throws Exception {
        byte[] b = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len = -1;
        while ((len = inputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

	/**
	 * 回调接口
	 * @author Administrator
	 *
	 */
	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String imageUrl);
	}

	
	
	public void recyleMemory() {
		for (Entry<String, SoftReference<Bitmap>> entry : imageCache.entrySet()) {
			SoftReference<Bitmap> item = entry.getValue();
			Bitmap map = item.get();
			if (map != null) {
				map.recycle();
			}
		}
	}

}
