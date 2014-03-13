/**
 * 
 */
package com.network.tools.toolbox;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.network.tools.dispatch.NetworkResponse;
import com.network.tools.dispatch.ParseError;
import com.network.tools.dispatch.Request;
import com.network.tools.dispatch.Response;

/**
 * A wrapped request used to load icon from installed packages or local APK files, when loads icon
 * successfully, it will be transferred to Bitmap and maintained in the BitmapLruCache.
 */
public class PackageIconRequest extends Request<Bitmap> {

	private final Context mContext;

	private final boolean mInstalledPackage;

	private final Response.Listener<Bitmap> mListener;

	/**
	 * Decoding lock so that we don't decode more than one image at a time (to avoid OOM's)
	 */
	private static final Object sDecodeLock = new Object();

	/**
	 * Create an instance of BitmapRequest.
	 * 
	 * @param context
	 *            The application context
	 * @param url
	 *            The image url to download.
	 * @param listener
	 *            the response listener.
	 * @param errorListener
	 *            the response error listener.
	 */
	public PackageIconRequest(Context context, String url, Response.Listener<Bitmap> listener,
			Response.ErrorListener errorListener, boolean installedPackage) {
		super(url, errorListener);
		mContext = context;
		mListener = listener;
		mInstalledPackage = installedPackage;
		setLocal(true);
	}

	@Override
	public Priority getPriority() {
		return Priority.LOW;
	}

	@Override
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
		synchronized (sDecodeLock) {
			if (mInstalledPackage) {
				return getPackageIcon(getUrl());
			} else {
				return getApkIcon(getUrl());
			}

		}
	}

	@Override
	protected void deliverResponse(Bitmap response) {
		mListener.onResponse(response);
	}

	/**
	 * Get bitmap response from installed package.
	 * 
	 * @param packageName
	 *            which package needs to load
	 * @return The bitmap response.
	 */
	private Response<Bitmap> getPackageIcon(String packageName) {
		try {
			PackageInfo pi = mContext.getPackageManager().getPackageInfo(packageName, 0);
			Drawable drawable = pi.applicationInfo.loadIcon(mContext.getPackageManager());
			if (drawable instanceof BitmapDrawable) {
				return Response.success(((BitmapDrawable) drawable).getBitmap(), null);
			}
		} catch (OutOfMemoryError e) {
			VolleyLog.e("Caught OOM for loading application icon , url=%s", packageName);
			return Response.error(new ParseError(e));
		} catch (NameNotFoundException e) {
			VolleyLog.e("A error occurred when load apk's icon [%s] >[%s].", packageName,
					e.getMessage());
			return Response.error(new ParseError(e));
		}
		return Response.error(new ParseError());
	}

	/**
	 * Get bitmap response from local APK file.
	 * 
	 * @param apkPath
	 *            The apkPath to load.
	 * 
	 * @return The bitmap response.
	 */
	private Response<Bitmap> getApkIcon(String apkPath) {
		try {
			Class<?> pkgParserCls = Class.forName("android.content.pm.PackageParser");
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { apkPath };
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			typeArgs = new Class<?>[] { File.class, String.class, DisplayMetrics.class, int.class };
			Method pkgParserParsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
					typeArgs);
			valueArgs = new Object[] { new File(apkPath), apkPath, metrics, 0 };
			Object pkgParserPkg = pkgParserParsePackageMtd.invoke(pkgParser, valueArgs);
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

			Class<?> assetMagCls = Class.forName("android.content.res.AssetManager");
			Object assetMag = assetMagCls.newInstance();
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMagAddAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
					typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMagAddAssetPathMtd.invoke(assetMag, valueArgs);
			Resources res = mContext.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);
			if (info != null) {
				if (info.icon != 0) {
					Drawable icon = res.getDrawable(info.icon);
					if (icon instanceof BitmapDrawable) {
						return Response.success(((BitmapDrawable) icon).getBitmap(), null);
					}
				}
			}
		} catch (OutOfMemoryError e) {
			VolleyLog.e("Caught OOM for loading application icon , url=%s", apkPath);
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			VolleyLog.e("A error occurred when load apk's icon [%s] >[%s].", apkPath,
					e.getMessage());
			return Response.error(new ParseError(e));
		}
		return Response.error(new ParseError());
	}

}
