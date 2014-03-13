package com.ninexiu.sixninexiu;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.network.tools.dispatch.Cache;
import com.network.tools.dispatch.RequestQueue;
import com.network.tools.toolbox.BasicNetwork;
import com.network.tools.toolbox.BitmapLoader;
import com.network.tools.toolbox.DiskBasedCache;
import com.network.tools.toolbox.HttpClientStack;
import com.ninexiu.utils.FinalBitmap;
import com.ninexiu.utils.NineXiuClient;
import com.ninexiu.utils.PreferenceFile;
import com.ninexiu.utils.UserManager;
import com.ninexiu.utils.Utils;

@SuppressLint("NewApi")
public class ApplicationEx extends Application {
	FinalBitmap finalBitmap;
	
	private static ApplicationEx mIns;
	private BitmapLoader mBitmapLoader;
	private Cache mCache;
	private Cache mBitmapCache;
	private RequestQueue mRequestQueue;
	
	private UserManager mUserManager;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/*public void displayBitmap(){
		// 获取应用程序最大可用内存  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();  
        int cacheSize = maxMemory / 8;
		finalBitmap = FinalBitmap.create(this.getApplicationContext()); //初始化  
		finalBitmap.configBitmapLoadThreadSize(3);//定义线程数量  
		finalBitmap.configDiskCachePath(this.getApplicationContext().getFilesDir().toString());//设置缓存目录；  
		finalBitmap.configDiskCacheSize(cacheSize);//设置缓存大小  
		finalBitmap.configRecycleImmediately(true);
		finalBitmap.configLoadingImage(R.drawable.loading_image);//设置加载图片  
	}

	public FinalBitmap getFinal(){
		return finalBitmap;
	}*/
	private RequestQueue mBitmapRequestQueue;
	@Override
	public void onCreate() {
		try {
			super.onCreate();
			mIns = this;
			mUserManager = new UserManager();
			PreferenceFile.init(this);
			mCache = new DiskBasedCache(getCacheDir("main"), 1024 * 1024);
			mRequestQueue = new RequestQueue(mCache, createNetwork());
			mRequestQueue.start();
			mBitmapCache = new DiskBasedCache(getCacheDir("images"), 4 * 1024 * 1024);
			mBitmapRequestQueue = new RequestQueue(mBitmapCache, createNetwork(), 4);
			mBitmapRequestQueue.start();
			mBitmapLoader = new BitmapLoader(mBitmapRequestQueue);
			
			FinalBitmap.CacheDir = 	Utils.getImageCacheDir(this).getAbsolutePath();

			//displayBitmap();
			/*// 获取应用程序最大可用内存
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSize = maxMemory / 8;
			Log.v("ApplicationEx", "memory use " + cacheSize);
			File cacheDir = StorageUtils.getOwnCacheDirectory(this, Utils
					.getImageCacheDir(this).getAbsolutePath());
			Log.v("ApplicationEx", "cacheDir=" + cacheDir);
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					this)
					// .memoryCacheExtraOptions(480, 800)
					// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
					.threadPoolSize(3)
					// default
					.threadPriority(Thread.NORM_PRIORITY - 1)
					// default
					.tasksProcessingOrder(QueueProcessingType.FIFO)
					// default
					.denyCacheImageMultipleSizesInMemory()
					.memoryCache(new LruMemoryCache(cacheSize))
					.memoryCacheSize(cacheSize)
					.discCache(new UnlimitedDiscCache(cacheDir))
					// default
					.discCacheSize(50 * 1024 * 1024)
					.discCacheFileCount(100)
					.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
					.imageDownloader(new BaseImageDownloader(this)) // default
					// .imageDecoder(new BaseImageDecoder()) // default
					.defaultDisplayImageOptions(
							DisplayImageOptions.createSimple()) // default
			// .enableLogging()
			;
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				builder.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
						.taskExecutorForCachedImages(
								AsyncTask.THREAD_POOL_EXECUTOR);
			}
			ImageLoaderConfiguration config = builder.build();*/
			/*
			 * defaultOptions = new DisplayImageOptions.Builder()
			 * .showStubImage(R.drawable.loading_image)
			 * //.showImageOnLoading(R.drawable.loading_image)
			 * .showImageForEmptyUri(R.drawable.loading_image)
			 * .showImageOnFail(R.drawable.loading_image) .cacheInMemory(true)
			 * //.cacheOnDisc(true) .resetViewBeforeLoading(false)
			 * //.considerExifParams(true) .bitmapConfig(Bitmap.Config.RGB_565)
			 * .build(); ImageLoaderConfiguration config = new
			 * ImageLoaderConfiguration.Builder(getApplicationContext())
			 * .defaultDisplayImageOptions(defaultOptions) .build();
			 */
			//ImageLoader.getInstance().init(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UserManager getUserManager() {
		return mUserManager;
	}
	
	public BitmapLoader getBitmapLoader() {
		return mBitmapLoader;
	}
	public static ApplicationEx get() {
		return mIns;
	}
	
	private BasicNetwork createNetwork() {
		return new BasicNetwork(new HttpClientStack(this));
	}
	
	private File getCacheDir(String suffix) {
		File dir = new File(getCacheDir(), suffix);
		dir.mkdirs();
		return dir;
	}
	
	@Override
	public void onLowMemory() {
		Log.v("ApplicationEx", "Ninexiu onLowMemory");
		super.onLowMemory();
		NineXiuClient.getInstance().shutdownHttpClient();
		System.gc();
		Runtime.getRuntime().gc();
	}

	

	@Override
	public void onTerminate() {
		Log.v("ApplicationEx", "Ninexiu onTerminate");
		super.onTerminate();
		NineXiuClient.getInstance().shutdownHttpClient();
	}
	

}
