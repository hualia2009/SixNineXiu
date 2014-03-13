/**
 * 
 */
package com.network.tools.toolbox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.network.tools.dispatch.DefaultRetryPolicy;
import com.network.tools.dispatch.NetworkResponse;
import com.network.tools.dispatch.ParseError;
import com.network.tools.dispatch.Request;
import com.network.tools.dispatch.Response;

/**
 * An implementation class of Request used to handle image request.
 */
public class ImageRequest extends Request<Bitmap> {

	private final int mMaxHeight;

	private final int mMaxWidth;

	private final int mRotate;

	private final boolean mRounded;

	private final Bitmap.Config mDecodeConfig;

	private final Response.Listener<Bitmap> mListener;

	/** Default number of retries for image requests */
	private static final int IMAGE_MAX_RETRIES = 2;

	/** Socket timeout in milliseconds for image requests */
	private static final int IMAGE_TIMEOUT_MS = 1000;

	/** Default backoff multiplier for image requests */
	private static final float IMAGE_BACKOFF_MULT = 2f;

	/**
	 * Decoding lock so that we don't decode more than one image at a time (to
	 * avoid OOM's)
	 */
	private static final Object sDecodeLock = new Object();

	/**
	 * Create an instance of ImageRequest.
	 * 
	 * @param url
	 *            The image url to download.
	 * @param maxWidth
	 *            the image width which you want to get.
	 * @param maxHeight
	 *            the image height which you want to get.
	 * @param rotate
	 *            the rotate corner.
	 * @param rounded
	 *            whether to show rounded bitmap or not.
	 * @param shouldCache
	 *            Needs to cache or not
	 * @param listener
	 *            the response listener.
	 * @param decodeConfig
	 *            the decodeConfig
	 * @param errorListener
	 *            the response error listener.
	 */
	public ImageRequest(String url, int maxWidth, int maxHeight, int rotate,
			boolean rounded, Response.Listener<Bitmap> listener,
			Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
		super(url, errorListener);
		mListener = listener;
		mRotate = rotate;
		mRounded = rounded;
		mMaxWidth = maxWidth;
		mMaxHeight = maxHeight;
		mDecodeConfig = decodeConfig;
		setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS,
				IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
	}

	@Override
	public Priority getPriority() {
		return Priority.LOW;
	}

	/**
	 * Scales one side of a rectangle to fit aspect ratio.
	 * 
	 * @param maxPrimary
	 *            Maximum size of the primary dimension (i.e. width for max
	 *            width), or zero to maintain aspect ratio with secondary
	 *            dimension
	 * @param maxSecondary
	 *            Maximum size of the secondary dimension, or zero to maintain
	 *            aspect ratio with primary dimension
	 * @param actualPrimary
	 *            Actual size of the primary dimension
	 * @param actualSecondary
	 *            Actual size of the secondary dimension
	 */
	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary) {
		// If no dominant value at all, just return the actual.
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		// If primary is unspecified, scale primary to match secondary's scaling
		// ratio.
		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	@Override
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
		synchronized (sDecodeLock) {
			try {
				return doParse(response);
			} catch (OutOfMemoryError e) {
				VolleyLog.e("Caught OOM for %d byte image, url=%s",
						new Object[] { Integer.valueOf(response.data.length),
								getUrl() });
				return Response.error(new ParseError(e));
			}
		}
	}

	private Response<Bitmap> doParse(NetworkResponse response) {
		byte[] data = response.data;
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (mMaxWidth == 0 && mMaxHeight == 0) {
			decodeOptions.inPreferredConfig = mDecodeConfig;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					decodeOptions);
		} else {
			// If we have to resize this image, first get the natural bounds.
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			// Then compute the dimensions we would ideally like to decode to.
			int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
					actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
					actualHeight, actualWidth);

			// Decode to the nearest power of two scaling factor.
			decodeOptions.inJustDecodeBounds = false;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
					actualHeight, desiredWidth, desiredHeight);
			Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length, decodeOptions);

			// Then scale to the exact desired size, if necessary.
			if (tempBitmap != null
					&& (tempBitmap.getWidth() != desiredWidth || tempBitmap
							.getHeight() != desiredHeight)) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
						desiredHeight, true);
				tempBitmap.recycle();
			} else {
				bitmap = tempBitmap;
			}
		}

		if (bitmap == null) {
			return Response.error(new ParseError());
		} else {
			if (mRotate != 0) {
				bitmap = rotateImage(bitmap, mRotate);
			}
			if (mRounded) {
				bitmap = toRoundedCorner(bitmap);
			}
			return Response.success(bitmap,
					HttpHeaderParser.parseCacheHeaders(response));
		}
	}

	@Override
	protected void deliverResponse(Bitmap response) {
		mListener.onResponse(response);
	}

	/**
	 * Returns the largest power-of-two divisor for use in downscaling a bitmap
	 * that will not result in the scaling past the desired dimensions.
	 * 
	 * @param actualWidth
	 *            Actual width of the bitmap
	 * @param actualHeight
	 *            Actual height of the bitmap
	 * @param desiredWidth
	 *            Desired width of the bitmap
	 * @param desiredHeight
	 *            Desired height of the bitmap
	 */
	// Visible for testing.
	static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		VolleyLog
				.v("actualWidth %d pix, actualHeight %d pix, desiredWidth %d pix, desiredHeight %d pix",
						actualWidth, actualHeight, desiredWidth, desiredHeight);
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}

		return (int) n;
	}

	/**
	 * Rotate the bitmap with specified angle;
	 * 
	 * @param bitmap
	 * @param rotate
	 *            the angle to rotate
	 * @return the rotated bitmap.
	 */
	static Bitmap rotateImage(Bitmap bitmap, int rotate) {
		Matrix matrix = new Matrix();
		matrix.setRotate(-rotate);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return newBitmap;
	}

	public static Bitmap toRoundedCorner(Bitmap bitmap) {
		Bitmap output = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		// final int color2 = Color.WHITE;
		// eyan removed
		final Paint paint = new Paint();
		// final Paint paint2 = new Paint();
		// paint.setStyle(Paint.Style.FILL_AND_STROKE);
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		// final Rect rect2 = new Rect(0, 0, bitmap.getWidth()-10,
		// bitmap.getHeight()-10);
		final RectF rectF = new RectF(rect);
		// final RectF rectF2 = new RectF(rect2);
		final float roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		// paint2.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// paint2.setColor(color2);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// paint2.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
		// canvas.drawCircle(roundPx, roundPx, roundPx+5, paint2);
		return output;
	}
	
	/*static Bitmap toRoundedCorner(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, 5, 5, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return output;
	}*/

}
