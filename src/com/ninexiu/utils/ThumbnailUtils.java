/**
 * 
 */
package com.ninexiu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;

import com.network.tools.toolbox.BitmapLoader;
import com.network.tools.toolbox.BitmapLoader.BitmapContainer;
import com.ninexiu.sixninexiu.R;

/**
 * The helper class used to add some special-effects for ImageView.
 */
public class ThumbnailUtils {

	/**
	 * Loads image with given url and binds it to ImageView.
	 * 
	 * @param urlToLoad
	 *            The image url to load.
	 * @param defaultImage
	 *            The defaultImage to set.
	 * @param bitmapLoader
	 *            The Bitmap loader.
	 * @param thumbnail
	 *            The ImageView to bind the bitmap.
	 */
	public static void setImage(String urlToLoad, Bitmap defaultImage, BitmapLoader bitmapLoader,
			final ImageView thumbnail) {
		Context context = thumbnail.getContext();
		Resources res = context.getResources();
		int width = res.getDimensionPixelSize(R.dimen.soft_entry_icon_width);
		int height = res.getDimensionPixelSize(R.dimen.soft_entry_icon_height);
		BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) thumbnail
				.getTag();
		if (oldContainer != null && oldContainer.getRequestUrl() != null) {
			oldContainer.cancelRequest();
		}
		BitmapLoader.BitmapContainer newContainer = bitmapLoader.get(urlToLoad, defaultImage,
				new BitmapLoader.BitmapLoadedHandler() {
					@Override
					public void onResponse(BitmapContainer result) {
						Bitmap tempBitmap = result.getBitmap();
						if (tempBitmap != null) {
							setImageBitmapWithFade(thumbnail, tempBitmap);
						}
					}
				}, width, height, 0, false);
		thumbnail.setVisibility(View.VISIBLE);
		thumbnail.setTag(newContainer);
		thumbnail.setImageBitmap(newContainer.getBitmap());
	}

	/**
	 * According to the packageName to load application's icon and bind it to ImageView.
	 * 
	 * @param packageName
	 *            The packageName to load application's icon
	 * @param defaultImage
	 *            The default image.
	 * @param bitmapLoader
	 * @param thumbnail
	 *            The ImageView needs to be binded.
	 */
	public static void setIcon(String packageName, Bitmap defaultImage, BitmapLoader bitmapLoader,
			final ImageView thumbnail) {
		Context context = thumbnail.getContext();
		Resources res = context.getResources();
		int width = res.getDimensionPixelSize(R.dimen.soft_entry_icon_width);
		int height = res.getDimensionPixelSize(R.dimen.soft_entry_icon_height);
		BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) thumbnail
				.getTag();
		if (oldContainer != null && oldContainer.getRequestUrl() != null) {
			oldContainer.cancelRequest();
		}
		BitmapLoader.BitmapContainer newContainer = bitmapLoader.get(context, packageName,
				defaultImage, new BitmapLoader.BitmapLoadedHandler() {
					@Override
					public void onResponse(BitmapContainer result) {
						Bitmap tempBitmap = result.getBitmap();
						if (tempBitmap != null) {
							thumbnail.setImageBitmap(tempBitmap);
						}
					}
				}, width, height, true);
		thumbnail.setVisibility(View.VISIBLE);
		thumbnail.setTag(newContainer);
		thumbnail.setImageBitmap(newContainer.getBitmap());
	}

	/**
	 * According to the apkPath to load application's icon and bind it to ImageView.
	 * 
	 * @param apkPath
	 *            The apkPath to load application's icon
	 * @param defaultImage
	 *            The default image.
	 * @param bitmapLoader
	 * @param thumbnail
	 *            The ImageView needs to be binded.
	 */
	public static void setApkIcon(String apkPath, Bitmap defaultImage, BitmapLoader bitmapLoader,
			final ImageView thumbnail) {
		Context context = thumbnail.getContext();
		Resources res = context.getResources();
		int width = res.getDimensionPixelSize(R.dimen.soft_entry_icon_width);
		int height = res.getDimensionPixelSize(R.dimen.soft_entry_icon_height);
		BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) thumbnail
				.getTag();
		if (oldContainer != null && oldContainer.getRequestUrl() != null) {
			oldContainer.cancelRequest();
		}
		BitmapLoader.BitmapContainer newContainer = bitmapLoader.get(context, apkPath,
				defaultImage, new BitmapLoader.BitmapLoadedHandler() {
					@Override
					public void onResponse(BitmapContainer result) {
						Bitmap tempBitmap = result.getBitmap();
						if (tempBitmap != null) {
							thumbnail.setImageBitmap(tempBitmap);
						}
					}
				}, width, height, false);
		thumbnail.setVisibility(View.VISIBLE);
		thumbnail.setTag(newContainer);
		thumbnail.setImageBitmap(newContainer.getBitmap());
	}

	/**
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	public static void setImageBitmapWithFade(ImageView imageView, Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(imageView.getResources(), bitmap);
		bd.setGravity(0x33);
		setImageDrawableWithFade(imageView, bd);
	}

	/**
	 * 
	 * @param imageView
	 * @param drawable
	 */
	public static void setImageDrawableWithFade(ImageView imageView, Drawable drawable) {
		Drawable oldDrawable = imageView.getDrawable();
		if (oldDrawable != null) {
			TransitionDrawable newDrawable = new TransitionDrawable(new Drawable[] { oldDrawable,
					drawable });
			newDrawable.setCrossFadeEnabled(true);
			imageView.setImageDrawable(newDrawable);
			newDrawable.startTransition(250);
		} else {
			imageView.setImageDrawable(drawable);
		}
	}

	public static void loadImage(String urlToLoad, Bitmap defaultImage, BitmapLoader bitmapLoader,
			final ImageView imageView, int width, int height, int rotate, boolean rounded) {
		BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) imageView
				.getTag();
		if (oldContainer != null && oldContainer.getRequestUrl() != null) {
			if (!oldContainer.getRequestUrl().equals(urlToLoad)) {
				oldContainer.cancelRequest();
			}
		}
		BitmapLoader.BitmapContainer newContainer = bitmapLoader.get(urlToLoad, defaultImage,
				new BitmapLoader.BitmapLoadedHandler() {
					@Override
					public void onResponse(BitmapContainer result) {
						Bitmap tempBitmap = result.getBitmap();
						if (tempBitmap != null) {
							imageView.setImageBitmap(tempBitmap);
						}
					}
				}, width, height, rotate, rounded);
		imageView.setVisibility(View.VISIBLE);
		imageView.setTag(newContainer);
		imageView.setImageBitmap(newContainer.getBitmap());
	}

	public static void loadImage(String urlToLoad, BitmapLoader bitmapLoader,
			final ImageView imageView, BitmapLoader.BitmapLoadedHandler bitmapLoadedHandler,
			int width, int height, int rotate, boolean rounded) {
		BitmapLoader.BitmapContainer oldContainer = (BitmapLoader.BitmapContainer) imageView
				.getTag();
		if (oldContainer != null && oldContainer.getRequestUrl() != null) {
			if (!oldContainer.getRequestUrl().equals(urlToLoad)) {
				oldContainer.cancelRequest();
			}
		}
		BitmapLoader.BitmapContainer newContainer = bitmapLoader.get(urlToLoad, null,
				bitmapLoadedHandler, width, height, rotate, rounded);
		imageView.setTag(newContainer);
	}
}
